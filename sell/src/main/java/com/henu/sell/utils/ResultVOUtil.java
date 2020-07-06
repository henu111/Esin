package com.henu.sell.utils;

import com.henu.sell.VO.ResultVO;

//public class ResultVOUtil {
//    public static ResultVO success(Object object){
//        ResultVO resultVO = new ResultVO();
///*   resultVO.setData(object);
//        resultVO.setCode(0);
//        resultVO.setMsg("成功");
//        return resultVO;*/
//
//        if (object!=null){
//            resultVO.setData(object);
//            resultVO.setCode(0);
//            resultVO.setMsg("成功");
//        }else {
//
//            resultVO.setData(null);
//            resultVO.setCode(1);
//            resultVO.setMsg("失败");
//        }
//        return resultVO;
//    }
//
///*public static ResultVO success(){
//        return success(null);
//    }
//    public static ResultVO error(Integer code,String msg){
//        ResultVO resultVO=new ResultVO();
//        resultVO.setCode(code);
//        resultVO.setMsg(msg);
//        return resultVO;
//    }*/
//}

public class ResultVOUtil {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}

