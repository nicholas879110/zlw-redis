//package com.zlw.cache.service;
//
//import java.io.Serializable;
//import java.util.Map;
//
///**
// * @Description
// * @Author zhangliewei
// * @Date 2016/8/6
// */
//public interface StringCacheService<String, T> extends CacheService<String, T> {
//
//
//    /**
//     * 向cache里添加一个字符串
//     *
//     * @param key   元素的key
//     * @param value 元素的值
//     * @return
//     */
//    public boolean putString(String key, String value) throws Exception;
//
//    /**
//     * @param key    元素key
//     * @param value  元素值
//     * @param expire 超时时间
//     * @return
//     * @throws Exception
//     */
//    public boolean putString(String key, String value, int expire) throws Exception;
//
//    /**
//     * 删除cache里的元素
//     *
//     * @param key 元素的key
//     * @return
//     */
//    public boolean deleteString(String key) throws Exception;
//
//    /**
//     * 获取cache里元素的值
//     *
//     * @param key 元素的key
//     * @return
//     */
//    public String getString(String key) throws Exception;
//
//
//    /**
//     * 向cache里添加一个元素
//     *
//     * @param key   元素的key
//     * @param value 元素的值
//     * @return
//     */
//    public <T extends Serializable> boolean putElement(String key, T value) throws Exception;
//
//
//    /**
//     * 向cache里添加一个元素
//     *
//     * @param key   元素的key
//     * @param value 元素的值
//     * @return
//     */
//    public boolean putElement(Object key, Object value, int expire) throws Exception;
//
//
//    /**
//     * @param key    元素key
//     * @param value  元素值
//     * @param expire 超时时间
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putElement(String key, T value, int expire) throws Exception;
//
//    /**
//     * 删除cache里的元素
//     *
//     * @param key 元素的key
//     * @return
//     */
//    public boolean deleteElement(String key) throws Exception;
//
//
//    /**
//     * 删除cache里的元素
//     *
//     * @param key 元素的key
//     * @return
//     */
//    public boolean deleteElement(Object key) throws Exception;
//
//
//    /**
//     * 删除cache里的元素
//     *
//     * @param keys 元素的key
//     * @return
//     */
//    public boolean deleteElements(byte[][] keys);
//
//
//    /**
//     * 获取cache里元素的值
//     *
//     * @param key 元素的key
//     * @return
//     */
//    public <T> T getElement(String key, Class<T> tClass) throws Exception;
//
//
//    /**
//     * 获取cache里元素的值
//     *
//     * @param key 元素的key
//     * @return
//     */
//    public <T> T getElement(Object key, Class<T> tClass) throws Exception;
//
//
//    /**
//     * 获取cache里元素的值
//     *
//     * @param key 元素的key
//     * @return
//     */
//    public Object getElement(Object key) throws Exception;
//
//
//    /**
//     * 缓存中是否存在元素
//     *
//     * @param key
//     * @return
//     */
//    public boolean exsitsElemet(Object key);
//
//    /**
//     * 向cache增加一个Map
//     *
//     * @param key
//     * @param map
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putMap(String key, Map<String, T> map) throws Exception;
//
//    /**
//     * 向缓存中的Map增加值
//     *
//     * @param key
//     * @param mkey
//     * @param value
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putMap(String key, String mkey, T value) throws Exception;
//
//
//    /**
//     * 从缓存中获取一个Map
//     *
//     * @param key
//     * @param mkey
//     * @param tClass
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public <T> T getMap(String key, String mkey, Class<T> tClass) throws Exception;
//
//
//    /**
//     * 从缓存Map中获取相应值
//     *
//     * @param key
//     * @param tClass
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public <T> Map<String, T> getMap(String key, Class<T> tClass) throws Exception;
//
//
//    /**
//     * 从缓存中删除Map
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public boolean delMap(String key) throws Exception;
//
//    /**
//     * 从缓存中Map中删除相应值
//     *
//     * @param key
//     * @param mkey
//     * @return
//     * @throws Exception
//     */
//    public boolean delMap(String key, String mkey) throws Exception;
//
//
//    /**
//     * 向缓存添加List
//     *
//     * @param key
//     * @param values
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putList(String key, List<T> values) throws Exception;
//
//    /**
//     * 向缓存中添加数组
//     *
//     * @param key
//     * @param value
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public <T extends Serializable> boolean putList(String key, T... value) throws Exception;
//
//    /**
//     * 从缓存中获取List
//     *
//     * @param key
//     * @param tClass
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public <T> List<T> getList(String key, Class<T> tClass) throws Exception;
//
//
//    /**
//     * 从缓存中List相应索引位置返回相应对象
//     *
//     * @param key
//     * @param index
//     * @param tClass
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public <T> T getList(String key, int index, Class<T> tClass) throws Exception;
//
//    /**
//     * 从缓存中List获取子List
//     *
//     * @param key
//     * @param start
//     * @param end
//     * @param tClass
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public <T> List<T> getList(String key, Integer start, Integer end, Class<T> tClass) throws Exception;
//
//    /**
//     * 从缓存中删除List
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public boolean delList(String key) throws Exception;
//
//
//    /**
//     * 从缓存中获取Set
//     *
//     * @param key
//     * @param tClass
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public <T> Set<T> getSet(String key, Class<T> tClass) throws Exception;
//
//
//    /**
//     * 从缓存中获取Set
//     *
//     * @param key
//     * @param start
//     * @param end
//     * @return
//     * @throws Exception
//     */
//    public Set getSet(String key, int start, int end);
//
//
//    /**
//     * 从缓存中获取Set
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public Set getSet(String key) throws Exception;
//
//
//    /**
//     * 查找所有符合匹配的keys
//     *
//     * @param patter
//     * @return
//     */
//    public Set keys(String patter);
//
//
//    public SerializationService getSerializationService();
//}
