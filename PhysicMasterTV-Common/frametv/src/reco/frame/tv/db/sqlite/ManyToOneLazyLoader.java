package reco.frame.tv.db.sqlite;

import reco.frame.tv.TvDb;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 涓?瀵瑰??寤惰?????杞界被
 * Created by pwy on 13-7-25.
 * @param <O> 瀹夸富瀹?浣????class
 * @param <M> 澶???惧??浣?class
 */
public class ManyToOneLazyLoader<M,O> {
    M manyEntity;
    Class<M> manyClazz;
    Class<O> oneClazz;
    TvDb db;
    /**
     * ??ㄤ??
     */
    private Object fieldValue;
    public ManyToOneLazyLoader(M manyEntity, Class<M> manyClazz, Class<O> oneClazz, TvDb db){
        this.manyEntity = manyEntity;
        this.manyClazz = manyClazz;
        this.oneClazz = oneClazz;
        this.db = db;
    }
    O oneEntity;
    boolean hasLoaded = false;

    /**
     * 濡??????版????????杞斤?????璋????loadManyToOne濉??????版??
     * @return
     */
    public O get(){
        if(oneEntity==null && !hasLoaded){
            this.db.loadManyToOne(null,this.manyEntity,this.manyClazz,this.oneClazz);
            hasLoaded = true;
        }
        return oneEntity;
    }
    public void set(O value){
        oneEntity = value;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}
