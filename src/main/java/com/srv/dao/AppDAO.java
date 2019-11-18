package com.srv.dao;

import com.srv.vo.TbAppVO;

import java.util.List;

public interface AppDAO {

    public List<TbAppVO> selectTbAppList(TbAppVO vo) throws Exception;
    public TbAppVO selectTbAppSingle(TbAppVO vo) throws Exception;
    public void insertTbAppMst(TbAppVO vo) throws Exception;
    public void updateTbAppMst(TbAppVO vo) throws Exception;
    public  void deleteTbAppMst(TbAppVO vo) throws Exception;
}
