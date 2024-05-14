package io.renren.modules.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/1 00:57
 */
public class SyncLineTokenVOData {

    @JsonProperty("ID")
    private Long id;
    @JsonProperty("CreatedAt")
    private String createdAt;
    @JsonProperty("UpdatedAt")
    private String updatedAt;
    @JsonProperty("DeletedAt")
    private String deletedAt;
    private Long userId;
    private String taskId;
    private String mid;
    private String countryCode;
    private String password;
    private String nickName;
    private String accessToken;
    private String token;
    private Long status;

    public SyncLineTokenVOData(Long id, String createdAt, String updatedAt, String deletedAt, Long userId, String taskId, String mid, String countryCode, String password, String nickName, String accessToken, String token, Long status) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.userId = userId;
        this.taskId = taskId;
        this.mid = mid;
        this.countryCode = countryCode;
        this.password = password;
        this.nickName = nickName;
        this.accessToken = accessToken;
        this.token = token;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public String getDeletedAt() {
        return this.deletedAt;
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public String getMid() {
        return this.mid;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getPassword() {
        return this.password;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getToken() {
        return this.token;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
