package com.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * JavaBean:PO，字段对应admin表
 * 
 * @author K
 */
@TableName("administer")
public class Admin {
	private int adminId;
	private String account;
	private String password;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}
	public int getAdminId() {
		return adminId;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccount() {
		return account;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	@Override
	public String toString() {
		return "Admin [account="+account+",password="+password+"]";
	}

}
