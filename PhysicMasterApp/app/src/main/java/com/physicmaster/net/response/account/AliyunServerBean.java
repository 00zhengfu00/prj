package com.physicmaster.net.response.account;

import com.physicmaster.net.response.Response;

import java.io.Serializable;

/**
 * Created by huashigen on 2016/11/21.
 */
public class AliyunServerBean extends Response implements Serializable {
    public String bucketName;
    public String hostId;
    public String imgPath;
    public String videoPath;
}
