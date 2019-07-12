//package com.zlw.redis;
//
//import com.zlw.cache.service.StringCacheService;
//import com.zlw.commons.serialization.SerializationService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisCluster;
//import redis.clients.jedis.JedisPool;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//
///**
// * @Description
// * @Author zhangliewei
// * @Date 2016/8/6
// * @Copyright(c)
// */
//public class CacheServiceJedisCluster implements StringCacheService {
//
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    private JedisCluster jedisCluster;
//    private SerializationService serializationService;
//
//    @Override
//    public boolean set(String key, String value) throws Exception {
//        log.info("方法[public boolean putString( String key, String value) throws Exception]参数：[key:{},value:{}]", key, value);
//        boolean status = false;
//        try {
//            String rstr = jedisCluster.set(key, value);
//            if (Objects.equals("OK", rstr)) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    @Override
//    public boolean putString(String key, String value, int expire) throws Exception {
//        log.info("方法[public boolean putString( String key, String value) throws Exception]参数：[key:{},value:{}]", key, value);
//        boolean status = false;
//        try {
//            String rstr = null;
//            rstr = jedisCluster.setex(key, expire, value);
//            if (Objects.equals("OK", rstr)) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    @Override
//    public boolean deleteString(String key) throws Exception {
//        log.info("方法[public boolean deleteString( String key) throws Exception]参数：[key:{}]", key);
//        boolean status = false;
//        try {
//            long r = jedisCluster.del(key);
//            if (r > 0) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    @Override
//    public String getString(String key) throws Exception {
//        log.info("方法[public <T> T getString( String key) throws Exception]参数：[key:{}]", key);
//        String result = null;
//        try {
//            result = jedisCluster.get(key);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return result;
//        }
//    }
//
//    @Override
//    public <T extends Serializable> boolean putElement(String key, T value) throws Exception {
//        log.info("方法[public boolean putElement( String key, Object value) throws Exception]参数：[key:{},value:{}]", key, value);
//        boolean status = false;
//        try {
//            String rstr = jedisCluster.set(key.getBytes(), serializationService.serialize(value));
//            if (Objects.equals("OK", rstr)) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    @Override
//    public boolean putElement(Object key, Object value, int expire) throws Exception {
//        log.info("方法[public boolean putElement( Object key, Object value) throws Exception]参数：[key:{},value:{}]", key, value);
//        boolean status = false;
//        try {
//            String rstr = jedisCluster.setex(serializationService.serialize(key), expire, serializationService.serialize(value));
//            if (Objects.equals("OK", rstr)) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    @Override
//    public <T extends Serializable> boolean putElement(String key, T value, int expire) throws Exception {
//        log.info("方法[public boolean putElement( String key, Object value) throws Exception]参数：[key:{},value:{}]", key, value);
//        boolean status = false;
//        try {
//            String rstr = null;
//            rstr = jedisCluster.setex(key.getBytes(), expire, serializationService.serialize(value));
//            if (Objects.equals("OK", rstr)) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    @Override
//    public boolean deleteElement(String key) throws Exception {
//        log.info("方法[public boolean deleteElement( String key) throws Exception]参数：[key:{}]", key);
//        boolean status = false;
//        try {
//            long r = jedisCluster.del(key.getBytes());
//            if (r > 0) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//
//    @Override
//    public boolean deleteElement(Object key) throws Exception {
//        log.info("方法[public boolean deleteElement( Object key) throws Exception]参数：[key:{}]", key);
//        boolean status = false;
//        try {
//            long r = jedisCluster.del(serializationService.serialize(key));
//            if (r > 0) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    @Override
//    public boolean deleteElements(byte[][] keys) {
//        log.info("方法[public boolean deleteElement( byte[][] key) throws Exception]参数：[keys:{}]", keys);
//        boolean status = false;
//        try {
//            long r = jedisCluster.del(keys);6
//            if (r > 0) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    @Override
//    public <T> T getElement(String key, Class<T> tClass) throws Exception {
//        log.info("方法[public <T> T getElement( String key, Class<T> tClass) throws Exception]参数：[key:{},Class:{}]", key, tClass.getName());
//        T result = null;
//        try {
//            byte[] bytes = jedisCluster.get(key.getBytes());
//            if (bytes != null) {
//                result = (T) serializationService.deserialize(bytes, tClass);
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return result;
//        }
//    }
//
//    @Override
//    public <T> T getElement(Object key, Class<T> tClass) throws Exception {
//        log.info("方法[public <T> T getElement( Object key, Class<T> tClass) throws Exception]参数：[key:{},Class:{}]", key, tClass.getName());
//        T result = null;
//        try {
//            byte[] bytes = jedisCluster.get(serializationService.serialize(key));
//            if (bytes != null) {
//                result = (T) serializationService.deserialize(bytes, tClass);
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return result;
//        }
//    }
//
//    @Override
//    public Object getElement(Object key) throws Exception {
//        log.info("方法[public <T> T getElement( String key) throws Exception]参数：[key:{}]", key);
//        Object result = null;
//        try {
//            byte[] bytes = jedisCluster.get(serializationService.serialize(key));
//            if (bytes != null) {
//                result = serializationService.deserialize(bytes);
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return result;
//        }
//    }
//
//
//    @Override
//    public boolean exsitsElemet(Object key) {
//        return jedisCluster.exists(serializationService.serialize(key));
//    }
//
//    /**
//     * 像指定缓存保存Map数据
//     *
//     * @param key
//     * @param map
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putMap(String key, Map<String, T> map) throws Exception {
//        log.info("方法[public boolean putMap( String key, Map<String, Object> map) throws Exception]参数：[key:{},Map size:{}]", key, map.size());
//        boolean status = false;
//        try {
//            if (!Objects.equals(null, map)) {
//                HashMap<byte[], byte[]> tmpMap = new HashMap<byte[], byte[]>();
//                for (Map.Entry<String, T> entry : map.entrySet()) {
//                    tmpMap.put(entry.getKey().getBytes(), serializationService.serialize(entry.getValue()));
//                }
//                String rstr = jedisCluster.hmset(key.getBytes(), tmpMap);
//                if (Objects.equals("OK", rstr)) {
//                    status = true;
//                }
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//
//    }
//
//    /**
//     * 像指定缓存保存key,value数据
//     *
//     * @param key
//     * @param mkey
//     * @param value
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putMap(String key, String mkey, T value) throws Exception {
//        log.info("方法[public boolean putMap( String key, String mkey, Object value) throws Exception]参数：[key:{},mkey:{},Object:{}]", key, mkey, value.toString());
//        long r = 0;
//        boolean status = false;
//        try {
//            r = jedisCluster.hset(key.getBytes(), mkey.getBytes(), serializationService.serialize(value));
//            if (r > 0) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    /**
//     * 在指定缓存获取Map中的数据
//     *
//     * @param key
//     * @param mkey
//     * @return
//     * @throws Exception
//     */
//    public <T> T getMap(String key, String mkey, Class<T> tClass) throws Exception {
//        log.info("方法[public List<String> getMap( String key, String mkey) throws Exception]参数：[key:{},mkey:{}]", key, mkey);
//        T result = null;
//        try {
//            byte[] bytes = jedisCluster.hget(key.getBytes(), mkey.getBytes());
//            result = (T) serializationService.deserialize(bytes, tClass);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return result;
//        }
//    }
//
//    /**
//     * 获取整个Map数据
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public <T> Map<String, T> getMap(String key, Class<T> tClass) throws Exception {
//        log.info("方法[public Map<String, String> getMap( String key) throws Exception]参数：[key:{}]", key);
//        Map<String, T> returnMap = new HashMap();
//        try {
//            Map<byte[], byte[]> map = jedisCluster.hgetAll(key.getBytes());
//            if (!Objects.equals(null, map)) {
//                Set<Map.Entry<byte[], byte[]>> entrySet = map.entrySet();
//                for (Map.Entry<byte[], byte[]> entry : entrySet) {
//                    returnMap.put((String) serializationService.deserialize(entry.getKey(), tClass),
//                            (T) serializationService.deserialize(entry.getValue(), tClass));
//                }
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return returnMap;
//        }
//    }
//
//    /**
//     * 删除Map
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public boolean delMap(String key) throws Exception {
//        log.info("方法[public boolean delMap( String key) throws Exception]参数：[key:{}]", key);
//        return deleteElement(key);
//    }
//
//    /**
//     * 删除map属性
//     *
//     * @param key
//     * @param mkey
//     * @return
//     * @throws Exception
//     */
//    public boolean delMap(String key, String mkey) throws Exception {
//        log.info("方法[public boolean delMap( String key, String mkey) throws Exception]参数：[key:{},mkey:{}]", key, mkey);
//        boolean status = false;
//        try {
//            long r = jedisCluster.hdel(key.getBytes(), mkey.getBytes());
//            if (r > 0) {
//                status = true;
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    /**
//     * 保存List
//     *
//     * @param key
//     * @param values
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putList(String key, List<T> values) throws Exception {
//        log.info("方法[public boolean putList( String key, List<Object> values) throws Exception]参数：[key:{},list size:{}]", key, values.size());
//        return putList(key, values.toArray());
//    }
//
//    /**
//     * 保存数组List
//     *
//     * @param key
//     * @param value
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putList(String key, T... value) throws Exception {
//        log.info("方法[public boolean putList( String key, Object... value) throws Exception]参数：[key:{},value:{}]", key, value);
//        boolean status = false;
//        try {
//            for (int i = 0; i < value.length; i++) {
//                jedisCluster.rpush(key.getBytes(), serializationService.serialize(value[i]));
//            }
//            status = true;
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return status;
//        }
//    }
//
//    /**
//     * 获取List
//     *
//     * @param key
//     * @return
//     */
//    public <T> List<T> getList(String key, Class<T> tClass) throws Exception {
//        log.info("方法[public List<String> getList( String key) throws Exception]参数：[key:{}]", key);
//        return getList(key, 0, -1, tClass);
//    }
//
//    /**
//     * 根据索引获取list对应属性
//     *
//     * @param key
//     * @param index
//     * @return
//     */
//    public <T> T getList(String key, int index, Class<T> tClass) throws Exception {
//        log.info("方法[public String getList( String key, int index) throws Exception]参数：[key:{},index{}]", key, index);
//        T t = null;
//        try {
//            t = (T) jedisCluster.lindex(key, index);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return t;
//        }
//    }
//
//    /**
//     * 获取指定起始位置的数组
//     *
//     * @param key
//     * @param start
//     * @param end
//     * @return
//     */
//    public <T> List<T> getList(String key, Integer start, Integer end, Class<T> tClass) throws Exception {
//        log.info("方法[public List<String> getList( String key, Integer start, Integer end) throws Exception]参数：[key:{},start:{},end:{}]", key, start, end);
//        List<T> returnList = new ArrayList();
//        try {
//            List<byte[]> list = jedisCluster.lrange(key.getBytes(), start, end);
//            if (!Objects.equals(null, list)) {
//                for (byte[] bytes : list) {
//                    T[] as = (T[]) serializationService.deserialize(bytes, tClass);
//                    returnList.addAll(Arrays.<T>asList(as));
//                }
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            return returnList;
//        }
//    }
//
//    /**
//     * 删除List
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public boolean delList(String key) throws Exception {
//        log.info("方法[public boolean delList( String key) throws Exception]参数：[key:{}]", key);
//        return deleteElement(key);
//    }
//
//
//    @Override
//    public <T> Set<T> getSet(String key, Class<T> tClass) throws Exception {
////        log.info("方法[public List<String> getList( String key, Integer start, Integer end) throws Exception]参数：[key:{},start:{},end:{}]", key, start, end);
////        List<T> returnList = new ArrayList();
////        try {
////            Set<byte[]> list = jedisCluster..srange(key.getBytes(), start, end);
////            if (!Objects.equals(null, list)) {
////                for (byte[] bytes : list) {
////                    T[] as = (T[])serializationService.deserialize(bytes,tClass);
////                    returnList.addAll(Arrays.<T>asList(as));
////                }
////            }
////        } catch (Exception e) {
////            throw e;
////        } finally {
////            return returnList;
////        }
//        return null;
//    }
//
//    @Override
//    public Set getSet(String key) throws Exception {
//        return null;
//    }
//
//    @Override
//    public Set getSet(String key, int start, int end) {
//        log.info("方法[public List<String> getSet( String key, Integer start, Integer end) throws Exception]参数：[key:{},start:{},end:{}]", key, start, end);
//        try {
//            Set<byte[]> sets = jedisCluster.zrange(key.getBytes(), start, end);
//            return sets;
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//
//    @Override
//    public Set keys(String pattern) {
//        log.info("方法[public Set keys( String pattern) throws Exception]参数：[pattern:{}]", pattern);
//        LinkedHashSet keys = new LinkedHashSet();
//        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
//        for (String k : clusterNodes.keySet()) {
//            log.debug("Getting keys from: {}", k);
//            JedisPool jp = clusterNodes.get(k);
//            Jedis connection = jp.getResource();
//            try {
//                keys.addAll(connection.keys(pattern));
//            } catch (Exception e) {
//                throw e;
//            } finally {
//                log.debug("Connection closed.");
//                connection.close();
//            }
//        }
//        return keys;
//    }
//
//
//    public JedisCluster getJedisCluster() {
//        return jedisCluster;
//    }
//
//    public void setJedisCluster(JedisCluster jedisCluster) {
//        this.jedisCluster = jedisCluster;
//    }
//
//    public SerializationService getSerializationService() {
//        return serializationService;
//    }
//
//    public void setSerializationService(SerializationService serializationService) {
//        this.serializationService = serializationService;
//    }
//}
