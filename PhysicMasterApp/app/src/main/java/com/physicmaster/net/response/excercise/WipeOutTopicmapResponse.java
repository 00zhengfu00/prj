package com.physicmaster.net.response.excercise;


import com.physicmaster.net.response.Response;

import java.io.Serializable;

/**
 * Created by songrui on 16/11/16.
 */

public class WipeOutTopicmapResponse extends Response {

    public DataBean data;

    public static class DataBean implements Serializable {
        public Integer quBatchId;
    }
}
