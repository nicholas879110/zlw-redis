package com.zlw.redis;

import com.zlw.cache.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CacheServiceTemplate<String, V> implements CacheService<String, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheServiceTemplate.class);

    /**
     * 出异常，重复操作的次数
     */
    private static final Integer TIMES = 3;

    private RedisTemplate redisTemplate;

    public CacheServiceTemplate() {
    }

    public CacheServiceTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void set(String key, V value) {
        redisTemplate.boundValueOps(key).set(value);
    }

    @Override
    public void set(String key, V value, Long expireTime) {
        redisTemplate.boundValueOps(key).set(value, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public boolean setExpireTime(String key, Long expireTime) {
        return redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public V get(String key) {
        Object obj = redisTemplate.boundValueOps(key).get();
        if (obj != null) {
            return (V) obj;
        }
        return null;
    }

    @Override
    public List<V> get(String... keys) {
        List<V> list = new ArrayList<V>();
        for (String key : keys) {
            Object obj = get(key);
            if (obj != null) {
                list.add((V) obj);
            }
        }
        return list;
    }

    @Override
    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public void remove(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                remove(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    @Override
    public void addList(String key, List<V> objectList) {
        for (V obj : objectList) {
            addList(key, obj);
        }
    }


    @Override
    public long addList(String key, V obj) {
        return redisTemplate.boundListOps(key).rightPush(obj);
    }

    @Override
    public long addList(String key, V... obj) {
        return redisTemplate.boundListOps(key).rightPushAll(obj);
    }

    @Override
    public List<V> getList(String key, long start, long end) {
        return redisTemplate.boundListOps(key).range(start, end);
    }

    @Override
    public List<V> getList(String key) {
        return redisTemplate.boundListOps(key).range(0, getListSize(key));
    }

    @Override
    public long getListSize(String key) {
        return redisTemplate.boundListOps(key).size();
    }

    @Override
    public long removeListValue(String key, V object) {
        return redisTemplate.boundListOps(key).remove(0, object);
    }

    @Override
    public long removeListValue(String key, V... objects) {
        long r = 0;
        for (V object : objects) {
            r += removeListValue(key, object);
        }
        return r;
    }

    @Override
    public void addMap(String key, Map<String, V> map) {
        redisTemplate.boundHashOps(key).putAll(map);
    }

    @Override
    public void addMap(String key, String field, V value) {
        redisTemplate.boundHashOps(key).put(field, value);
    }

    @Override
    public void addMap(String key, String field, V value, long time) {
        redisTemplate.boundHashOps(key).put(field, value);
        redisTemplate.boundHashOps(key).expire(time, TimeUnit.SECONDS);
    }


    @Override
    public void removeMapField(String key, V... field) {
        redisTemplate.boundHashOps(key).delete(field);
    }

    @Override
    public Long getMapSize(String key) {
        return redisTemplate.boundHashOps(key).size();
    }

    @Override
    public Map<String, V> getMap(String key) {
        return redisTemplate.boundHashOps(key).entries();
    }

    @Override
    public V getMapField(String key, String field) {
        Object obj = redisTemplate.boundHashOps(key).get(field);
        if (obj != null) {
            return (V) obj;
        }
        return null;
    }

    @Override
    public Boolean hasMapKey(String key, String field) {
        return redisTemplate.boundHashOps(key).hasKey(field);
    }

    @Override
    public List<V> getMapFieldValue(String key) {
        return redisTemplate.boundHashOps(key).values();
    }

    @Override
    public Set<V> getMapFieldKey(String key) {
        return redisTemplate.boundHashOps(key).keys();
    }


    @Override
    public void addSet(String key, V... obj) {
        redisTemplate.boundSetOps(key).add(obj);
    }

    @Override
    public Boolean setSetExpireTime(String key, Long time) {
        return redisTemplate.boundSetOps(key).expire(time, TimeUnit.SECONDS);
    }

    @Override
    public long removeSetValue(String key, V obj) {
        return redisTemplate.boundSetOps(key).remove(obj);
    }

    @Override
    public long removeSetValue(String key, V... obj) {
        if (obj != null && obj.length > 0) {
            return redisTemplate.boundSetOps(key).remove(obj);
        }
        return 0L;
    }

    @Override
    public long getSetSize(String key) {
        return redisTemplate.boundSetOps(key).size();
    }

    @Override
    public Boolean hasSetValue(String key, V obj) {
        Boolean boo = null;
        int t = 0;
        while (true) {
            try {
                boo = redisTemplate.boundSetOps(key).isMember(obj);
                break;
            } catch (Exception e) {
                log.error("key[" + key + "],obj[" + obj + "]判断Set中的值是否存在失败,异常信息:" + e.getMessage());
                t++;
            }
            if (t > TIMES) {
                break;
            }
        }
        log.info("key[" + key + "],obj[" + obj + "]是否存在,boo:" + boo);
        return boo;
    }

    @Override
    public Set<V> getSet(String key) {
        return redisTemplate.boundSetOps(key).members();
    }

    @Override
    public Set<V> getSetUnion(String key, String otherKey) {
        return redisTemplate.boundSetOps(key).union(otherKey);
    }

    @Override
    public Set<V> getSetUnion(String key, Set<Object> set) {
        return redisTemplate.boundSetOps(key).union(set);
    }

    @Override
    public Set<V> getSetIntersect(String key, String otherKey) {
        return redisTemplate.boundSetOps(key).intersect(otherKey);
    }

    @Override
    public Set<V> getSetIntersect(String key, Set<Object> set) {
        return redisTemplate.boundSetOps(key).intersect(set);
    }


    @Override
    public Boolean addZSet(String key, double score, V value) {
        return redisTemplate.boundZSetOps(key).add(value, score);
    }

    @Override
    public Long addZSet(String key, TreeSet<V> value) {
        return redisTemplate.boundZSetOps(key).add(value);
    }

    @Override
    public Boolean addZSet(String key, double[] score, V[] value) {
        if (score.length != value.length) {
            return false;
        }
        for (int i = 0; i < score.length; i++) {
            if (addZSet(key, score[i], value[i]) == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean setZSetExpireTime(String key, Long time) {
        return redisTemplate.boundZSetOps(key).expire(time, TimeUnit.SECONDS);
    }


    @Override
    public long getZSetSize(String key) {
        return redisTemplate.boundZSetOps(key).size();
    }

    @Override
    public Long removeZSetValue(String key, V... value) {
        return redisTemplate.boundZSetOps(key).remove(value);
    }

    @Override
    public void removeZSet(String key) {
        removeZSetRange(key, 0L, getZSetSize(key));
    }

    @Override
    public void removeZSetRange(String key, Long start, Long end) {
        redisTemplate.boundZSetOps(key).removeRange(start, end);
    }


    @Override
    public void setZSetUnionAndStore(String key, String key1, String key2) {
        redisTemplate.boundZSetOps(key).unionAndStore(key1, key2);
    }

    @Override
    public Set<V> getZSetRange(String key) {
        return getZSetRange(key, 0, getZSetSize(key));
    }


    @Override
    public Set<V> getZSetRange(String key, long s, long e) {
        return redisTemplate.boundZSetOps(key).range(s, e);
    }

    @Override
    public Set<V> getZSetReverseRange(String key) {
        return getZSetReverseRange(key, 0, getZSetSize(key));
    }

    @Override
    public Set<V> getZSetReverseRange(String key, long start, long end) {
        return redisTemplate.boundZSetOps(key).reverseRange(start, end);
    }

    @Override
    public Set<V> getZSetRangeByScore(String key, double start, double end) {
        return redisTemplate.boundZSetOps(key).rangeByScore(start, end);
    }

    @Override
    public Set<V> getZSetReverseRangeByScore(String key, double start, double end) {
        return redisTemplate.boundZSetOps(key).reverseRangeByScore(start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> getZSetRangeWithScores(String key, long start, long end) {
        return redisTemplate.boundZSetOps(key).rangeWithScores(start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> getZSetReverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.boundZSetOps(key).reverseRangeWithScores(start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> getZSetRangeWithScores(String key) {
        return getZSetRangeWithScores(key, 0, getZSetSize(key));
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> getZSetReverseRangeWithScores(String key) {
        return getZSetReverseRangeWithScores(key, 0, getZSetSize(key));
    }

    @Override
    public long getZSetCountSize(String key, double sMin, double sMax) {
        return redisTemplate.boundZSetOps(key).count(sMin, sMax);
    }


    @Override
    public double getZSetScore(String key, V value) {
        return redisTemplate.boundZSetOps(key).score(value);
    }

    @Override
    public double incrementZSetScore(String key, V value, double delta) {
        return redisTemplate.boundZSetOps(key).incrementScore(value, delta);
    }


    @Override
    public void removeZSetRangeByScore(String key, double s, double e) {
        redisTemplate.boundZSetOps(key).removeRangeByScore(s, e);
    }


    @Override
    public DataType getType(String key) {
        return redisTemplate.type(key);
    }


    @Override
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }


}
