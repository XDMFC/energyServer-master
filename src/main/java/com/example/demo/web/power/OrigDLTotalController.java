package com.example.demo.web.power;


import com.example.demo.entity.ExportPower;
import com.example.demo.entity.ExportTotal;
import com.example.demo.entity.Params;
import com.example.demo.service.power.OrigDLService;
import com.example.demo.service.power.OrigDLTotalService;
import com.example.demo.util.MyException.Result;
import com.example.demo.util.MyException.ResultUtil;
import com.example.demo.util.date.DateUtil;
import com.github.pagehelper.PageInfo;
import com.wuwenze.poi.ExcelKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lwx
 * @since 2019-05-08
 */
@Controller
@RequestMapping("/api/power")
public class OrigDLTotalController {

    @Autowired
    private OrigDLTotalService origDLTotalService;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/PowerByRatio.do",produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    @ResponseBody
    public Result getEDepartmentData(@RequestBody Params param) {
        logger.info(param.getEMeterName()+","+param.getPageNum()+","+param.getPageSize()+","+param.getBeginTime()+","+param.getEndTime()+","+param.getPowerType()+","+param.getEStationName());
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        PageInfo<Map> page = this.origDLTotalService.getEDepartmentData(param);
        System.out.println(page.getList().size());
        resultMap.put("tableData", page);
        Result result = ResultUtil.success();
        result.setData(resultMap);
        return result;
    }

    @RequestMapping(value = "/exportPowerByRatio.do", method = RequestMethod.GET)
    public void getEDepartmentDataByRatio(HttpServletRequest request, HttpServletResponse response) {
        Params params = new Params();
        String beginTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        params.setEMeterName(request.getParameter("emeterName"));
        params.setEStationName(request.getParameter("estationName"));
        params.setPowerType(request.getParameter("powerType"));
        params.setBeginTime(DateUtil.parseGMT(beginTime));
        params.setEndTime(DateUtil.parseGMT(endTime));
        // List<ExportPower> list = origDLService.exportPowerTotal(params);
        params.setPageNum("1");
        params.setPageSize("50000000");
        List<Map> list = origDLTotalService.getEDepartmentData(params).getList();
        ExcelKit.$Export(ExportTotal.class, response).downXlsx(list, false);
    }



}
