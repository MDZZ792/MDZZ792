package com.test.Service.Impl;

import com.test.Dao.PrometheusDao;
import com.test.Service.PrometheusService;

import javax.inject.Inject;

import java.util.List;

import static com.test.util.JsonPrometheus.MatrixJson;


/**
 * @ClassName PrometheusServiceImpl
 * @Atuhor MDZZ_792 (｢･ω･)｢ 嘿
 * @Time 上午11:28 18-10-15
 */
public class PrometheusServiceImpl implements PrometheusService {
    @Inject
    private PrometheusDao prometheusDao;

    @Override
    public String GetJvm_Thread_CpuUsage(String... time) {
        String getJvm_thread_cpuUsage = null;
        boolean status = true;
        String result = "";
        f1:
            for(int i=0;i<time.length;i++){
                if(null == time[i]||time[i] == ""){
                    getJvm_thread_cpuUsage = prometheusDao.GetJvm_Thread_CpuUsage();
                    status = false;
                    break f1;
                }
            }
        if(status){
            getJvm_thread_cpuUsage = prometheusDao.GetJvm_Thread_CpuUsage(time);
        }
        List<String> matrixJson = MatrixJson(getJvm_thread_cpuUsage);
        for(int i=0;i<matrixJson.size();i++){
            result = result + matrixJson.get(i) + "|";
        }
        return result.substring(0,result.length()-1);
    }
}
