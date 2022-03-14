package com.welldo.boot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class User {

	private Long id;

	private String email;
	private String password;
	private String name;

	private long createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedDateTime() {
		return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault())
				.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}


	public String getImageUrl() {
		String url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fd5cfbe4767c5c81d5efd71067d6e57e93debf9b310050-T3UUFB_fw658&refer=http%3A%2F%2Fhbimg.b0.upaiyun.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1648983666&t=b94ee3257640f7ea016edbeb5364cecb";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(this.email.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
			return url + String.format("%032x", new BigInteger(1, hash));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	//如果要允许输入password，但不允许输出password，
	// 即在JSON序列化和反序列化时，允许写属性，禁用读属性，可以更精细地控制如下：
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("User[id=%s, email=%s, name=%s, password=%s, createdAt=%s, createdDateTime=%s]", getId(),
				getEmail(), getName(), getPassword(), getCreatedAt(), getCreatedDateTime());
	}
}
