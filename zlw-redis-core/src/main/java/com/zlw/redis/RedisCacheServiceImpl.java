//package com.zlw.redis;
//
//import com.zlw.cache.service.StringCacheService;
//import com.zlw.commons.serialization.SerializationService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
//import java.io.Serializable;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * redis单机缓存实现
// */
//public class RedisCacheServiceImpl implements StringCacheService {
//
//    private static Logger log = LoggerFactory.getLogger(RedisCacheServiceImpl.class);
//
//    private Map<Integer, JedisPool> poolMap = new ConcurrentHashMap();
//
//    private Map<String,Integer> cacheMapper;
//
//    private String hostName = "localhost";
//
//    private Integer port = 6379 ;
//
//    private Integer timeout = 2000 ;
//
//    private String password;
//
//    private JedisPoolConfig jedisPoolConfig;
//
//    private Integer datasize = 16;
//
//    private Integer database=0;
//
//    private SerializationService serializationService;
//
//    public RedisCacheServiceImpl() {
//    }
//
//    public Map<String, Integer> getCacheMapper() {
//        return cacheMapper;
//    }
//
//    public void setCacheMapper(Map<String, Integer> cacheMapper) {
//        this.cacheMapper = cacheMapper;
//    }
//
//    private void init() {
//        try {
//            JedisPool jedisPool = new JedisPool(jedisPoolConfig, getHostName(),getPort(),
//                    getTimeout(), password, database);
//            poolMap.put(database,jedisPool);
//        } catch (Exception e) {
//            log.error("Redis 缓存初始化错误:{}", e.getMessage(), e);
//        }
//    }
//
//    public boolean addCache(Integer database) throws Exception {
//        log.info("方法[public boolean addCache(String database) throws Exception]参数：[database:{}]", database);
//        JedisPool jedisPool = poolMap.get(database);
//        if (Objects.equals(null, jedisPool)) {
//            jedisPool = new JedisPool(getJedisPoolConfig(), getHostName(), getPort(),
//                    10000, password, database);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean deleteCache(Integer database) throws Exception {
//        log.info("方法[public boolean deleteCache(Integer database) throws Exception]参数：[database:{}]", database);
//        JedisPool jedisPool = poolMap.get(database);
//        if (!Objects.equals(null, jedisPool)) {
//            poolMap.get(database).getResource().flushDB();
//            poolMap.remove(database);
//            return true;
//        } else {
//            throw new Exception("Redis 缓存不存在");
//        }
//    }
//
//    @Override
//    public <T extends Serializable> boolean putElement(String key, T value) throws Exception {
//        log.info("方法[public boolean putElement( String key, Object value) throws Exception]参数：[cacheName:{}，key:{},value:{}]",  key, value);
//        boolean status = false;
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        try {
//            String rstr = null;
//            rstr = jedis.set(key.getBytes(), serializationService.serialize(value));
//            if (Objects.equals("OK", rstr)) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return status;
//    }
//
//    @Override
//    public <T extends Serializable> boolean putElement( String key,T value, int expire) throws Exception {
//        log.info("方法[public boolean putElement( String key, Object value) throws Exception]参数：[cacheName:{}，key:{},value:{}]",  key, value);
//        boolean status = false;
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        try {
//            String rstr = null;
//            rstr = jedis.setex(key.getBytes(),expire, serializationService.serialize(value));
//            if (Objects.equals("OK", rstr)) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return status;
//    }
//
//    @Override
//    public boolean deleteElement( String key) throws Exception {
//        log.info("方法[public boolean deleteElement( String key) throws Exception]参数：[key:{}]",  key);
//        boolean status = false;
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        try {
//            long r = jedis.del(key);
//            if (r > 0) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return status;
//    }
//
//    @Override
//    public <T> T getElement( String key, Class<T> tClass) throws Exception {
//        log.info("方法[public <T> T getElement( String key, Class<T> tClass) throws Exception]参数：[key:{},Class:{}]",  key, tClass.getName());
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        T result = null;
//        try {
//            byte[] bytes = jedis.get(key.getBytes());
//            if(bytes!=null) {
//                result = (T) serializationService.deserialize(bytes);
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return result;
//    }
//
//    /**
//     * 像指定缓存保存Map数据
//     *
//     *
//     * @param key
//     * @param map
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putMap( String key, Map<String, T> map) throws Exception {
//        log.info("方法[public boolean putMap( String key, Map<String, Object> map) throws Exception]参数：[key:{},Map size:{}]",  key, map.size());
//        boolean status = false;
//        if (!Objects.equals(null, map)) {
//            for (Map.Entry<String, T> entry : map.entrySet()) {
//                putMap( key, entry.getKey(), entry.getValue());
//            }
//            status = true;
//        }
//        return status;
//    }
//
//    /**
//     * 像指定缓存保存key,value数据
//     *
//     *
//     * @param key
//     * @param mkey
//     * @param value
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putMap( String key, String mkey, T value) throws Exception {
//        log.info("方法[public boolean putMap( String key, String mkey, Object value) throws Exception]参数：[key:{},mkey:{},Object:{}]",  key, mkey, value.toString());
//        long r = 0;
//        boolean status = false;
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        try {
//            r = jedis.hset(key.getBytes(), mkey.getBytes(), serializationService.serialize(value));
//            if (r > 0) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return status;
//    }
//
//    /**
//     * 在指定缓存获取Map中的数据
//     *
//     *
//     * @param key
//     * @param mkey
//     * @return
//     * @throws Exception
//     */
//    public <T> T getMap( String key, String mkey, Class<T> tClass) throws Exception {
//        log.info("方法[public List<String> getMap( String key, String mkey) throws Exception]参数：[key:{},mkey:{}]",  key, mkey);
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        T result = null;
//        try {
//            byte[] bytes = jedis.hget(key.getBytes(), mkey.getBytes());
//            result = (T) serializationService.deserialize(bytes);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return result;
//    }
//
//    /**
//     * 获取整个Map数据
//     *
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public <T> Map<String, T> getMap( String key, Class<T> tClass) throws Exception {
//        log.info("方法[public Map<String, String> getMap( String key) throws Exception]参数：[key:{}]",  key);
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        Map<String, T> returnMap = new HashMap();
//        try {
//            Map<byte[], byte[]> map = jedis.hgetAll(key.getBytes());
//            if (!Objects.equals(null, map)) {
//                Set<Map.Entry<byte[], byte[]>> entrySet = map.entrySet();
//                for (Map.Entry<byte[], byte[]> entry : entrySet) {
//                    returnMap.put((String) serializationService.deserialize(entry.getKey()),
//                            (T) serializationService.deserialize(entry.getValue()));
//                }
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return returnMap;
//    }
//
//    /**
//     * 删除Map
//     *
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public boolean delMap( String key) throws Exception {
//        log.info("方法[public boolean delMap( String key) throws Exception]参数：[key:{}]",  key);
//        return deleteElement( key);
//    }
//
//    /**
//     * 删除map属性
//     * @param key
//     * @param mkey
//     * @return
//     * @throws Exception
//     */
//    public boolean delMap( String key, String mkey) throws Exception {
//        log.info("方法[public boolean delMap( String key, String mkey) throws Exception]参数：[key:{},mkey:{}]",  key, mkey);
//        boolean status = false;
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        try {
//            long r = jedis.hdel(key, mkey);
//            if (r > 0) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return status;
//    }
//
//    /**
//     * 保存List
//     *
//     *
//     * @param key
//     * @param values
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putList( String key, List<T> values) throws Exception {
//        log.info("方法[public boolean putList( String key, List<Object> values) throws Exception]参数：[key:{},list size:{}]",  key, values.size());
//        return putList( key, values.toArray());
//    }
//
//    /**
//     * 保存数组List
//     *
//     *
//     * @param key
//     * @param value
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putList( String key, T... value) throws Exception {
//        log.info("方法[public boolean putList( String key, Object... value) throws Exception]参数：[key:{},value:{}]",  key, value);
//        boolean status = false;
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        try {
//            for (int i = 0; i < value.length; i++) {
//                jedis.rpush(key.getBytes(), serializationService.serialize(value[i]));
//            }
//            status = true;
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return status;
//    }
//
//    /**
//     * 获取List
//     *
//     *
//     * @param key
//     * @return
//     */
//    public <T> List<T> getList( String key,Class<T> tClass) throws Exception {
//        log.info("方法[public List<String> getList( String key) throws Exception]参数：[key:{}]",  key);
//        return getList( key, 0, -1,tClass);
//    }
//
//    /**
//     * 根据索引获取list对应属性
//     *
//     *
//     * @param key
//     * @param index
//     * @return
//     */
//    public <T> T getList( String key, int index, Class<T> tClass) throws Exception {
//        log.info("方法[public String getList( String key, int index) throws Exception]参数：[key:{},index{}]",  key, index);
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        T t = null;
//        try {
//            t = (T) jedis.lindex(key.getBytes(), index);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return t;
//    }
//
//    /**
//     * 获取指定起始位置的数组
//     *
//     *
//     * @param key
//     * @param start
//     * @param end
//     * @return
//     */
//    public <T> List<T> getList( String key, Integer start, Integer end, Class<T> tClass) throws Exception {
//        log.info("方法[public List<String> getList( String key, Integer start, Integer end) throws Exception]参数：[key:{},start:{},end:{}]",  key, start, end);
//        JedisPool pool = getJedisPool(database);
//        Jedis jedis = pool.getResource();
//        List<T> returnList = new ArrayList();
//        try {
//            List<byte[]> list = jedis.lrange(key.getBytes(), start, end);
//            if (!Objects.equals(null, list)) {
//                for (byte[] bytes : list) {
//                    T[] as = (T[])serializationService.deserialize(bytes);
//                    returnList.addAll(Arrays.<T>asList(as));
//                }
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            returnResource(jedis, pool);
//        }
//        return returnList;
//    }
//
//    /**
//     * 删除List
//     *
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public boolean delList( String key) throws Exception {
//        log.info("方法[public boolean delList( String key) throws Exception]参数：[key:{}]",  key);
//        return deleteElement( key);
//    }
//
//    public JedisPool  getJedisPool(Integer database) throws Exception {
//        JedisPool jedisPool = poolMap.get(database);
//        if (Objects.equals(null, jedisPool)) {
//            addCache(database);
//            jedisPool = poolMap.get(database);
//        }
//        return jedisPool;
//    }
//
//    public Long getDB(Integer database) throws Exception {
//        return getJedisPool(database).getResource().getDB();
//    }
//
//    public void flushDB(Integer database) throws Exception {
//        log.info("方法[public boolean flushDB(String cacheName) throws Exception]参数：[database:{}]", database);
//        JedisPool jedisPool=getJedisPool(database);
//        if (jedisPool!=null){
//            jedisPool.getResource().flushDB();
//        }
//    }
//
//    @Override
//    public boolean putString(String key, String value) throws Exception {
//        return false;
//    }
//
//    @Override
//    public boolean putString(String key, String value, int expire) throws Exception {
//        return false;
//    }
//
//    @Override
//    public boolean deleteString(String key) throws Exception {
//        return false;
//    }
//
//    @Override
//    public String getString(String key) throws Exception {
//        return null;
//    }
//
//
//    @Override
//    public boolean deleteElement(Object key) throws Exception {
//        return false;
//    }
//
//    @Override
//    public <T> T getElement(Object key, Class<T> tClass) throws Exception {
//        return null;
//    }
//
//    @Override
//    public Object getElement(Object key) throws Exception {
//        return null;
//    }
//
//    @Override
//    public boolean exsitsElemet(Object key) {
//        return false;
//    }
//
//    @Override
//    public <T> Set<T> getSet(String key, Class<T> tClass) throws Exception {
//        return null;
//    }
//
//    @Override
//    public Set getSet(String key) throws Exception {
//        return null;
//    }
//
//    @Override
//    public Set keys(String patter) {
//        return null;
//    }
//
//    public void returnResource(Jedis jedis, JedisPool pool) {
//        pool.returnResourceObject(jedis);
//    }
//
//    public String getHostName() {
//        return hostName;
//    }
//
//    public void setHostName(String hostName) {
//        this.hostName = hostName;
//    }
//
//    public JedisPoolConfig getJedisPoolConfig() {
//        return jedisPoolConfig;
//    }
//
//    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
//        this.jedisPoolConfig = jedisPoolConfig;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public Integer getPort() {
//        return port;
//    }
//
//    public void setPort(Integer port) {
//        this.port = port;
//    }
//
//    public Integer getTimeout() {
//        return timeout;
//    }
//
//    public void setTimeout(Integer timeout) {
//        this.timeout = timeout;
//    }
//
//    public Integer getDatasize() {
//        return datasize;
//    }
//
//    public void setDatasize(Integer datasize) {
//        this.datasize = datasize;
//    }
//
//    public Integer getDatabase() {
//        return database;
//    }
//
//    public void setDatabase(Integer database) {
//        this.database = database;
//    }
//
//    @Override
//    public SerializationService getSerializationService() {
//        return serializationService;
//    }
//
//    public void setSerializationService(SerializationService serializationService) {
//        this.serializationService = serializationService;
//    }
//
//    @Override
//    public boolean putElement(Object key, Object value, int expire) throws Exception {
//        return false;
//    }
//
//    @Override
//    public boolean deleteElements(byte[][] keys) {
//return false;
//    }
//
//    @Override
//    public Set getSet(String key, int start, int end) {
//        return null;
//    }
//}
