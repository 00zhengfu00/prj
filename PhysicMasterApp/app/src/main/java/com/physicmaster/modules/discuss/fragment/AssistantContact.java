package com.physicmaster.modules.discuss.fragment;

import android.text.TextUtils;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.Map;

/**
 * Created by huashigen on 2017/5/22.
 */

public class AssistantContact implements RecentContact {
    private String contactId;
    private String fromAccount;
    private String fromNick;
    private SessionTypeEnum sessionType;
    private String recentMessageId;
    private MsgTypeEnum msgType;
    private MsgStatusEnum msgStatus;
    private int unreadCount;
    private String content;
    private long time;
    private MsgAttachment attachment;
    private long tag;
    private Map<String, Object> extension;

    public void setBackupName(String backupName) {
        this.backupName = backupName;
    }

    private String backupName;

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public void setFromNick(String fromNick) {
        this.fromNick = fromNick;
    }

    public void setSessionType(SessionTypeEnum sessionType) {
        this.sessionType = sessionType;
    }

    public void setRecentMessageId(String recentMessageId) {
        this.recentMessageId = recentMessageId;
    }

    public void setMsgType(MsgTypeEnum msgType) {
        this.msgType = msgType;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setAttachment(MsgAttachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public String getContactId() {
        return contactId;
    }

    @Override
    public String getFromAccount() {
        return fromAccount;
    }

    @Override
    public String getFromNick() {
        if (TextUtils.isEmpty(fromNick)) {
            return backupName;
        }
        return fromNick;
    }

    @Override
    public SessionTypeEnum getSessionType() {
        return sessionType;
    }

    @Override
    public String getRecentMessageId() {
        return recentMessageId;
    }

    @Override
    public MsgTypeEnum getMsgType() {
        return msgType;
    }

    @Override
    public MsgStatusEnum getMsgStatus() {
        return msgStatus;
    }

    @Override
    public void setMsgStatus(MsgStatusEnum msgStatusEnum) {
        msgStatus = msgStatusEnum;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public MsgAttachment getAttachment() {
        return attachment;
    }

    @Override
    public void setTag(long l) {
        this.tag = l;
    }

    @Override
    public long getTag() {
        return tag;
    }

    @Override
    public Map<String, Object> getExtension() {
        return extension;
    }

    @Override
    public void setExtension(Map<String, Object> map) {
        this.extension = map;
    }

}
