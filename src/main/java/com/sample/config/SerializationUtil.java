package com.sample.config;

import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

@Component
@Primary
public class SerializationUtil implements RedisSerializer<Object> {

	@Override
	public byte[] serialize(Object t) throws SerializationException {
		return SerializationUtils.serialize(t);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		return SerializationUtils.deserialize(bytes);
	}

}
