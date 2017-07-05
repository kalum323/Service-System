/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.care_point.service.job_card;

import com.mac.care_point.service.job_card.model.JobSummary;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author HP
 */
public interface VehicleRegistrarRepository {
    
    @Query("select job from JobSummary job where job.vehicleNo like %:vehicleNo%")
    public List<JobSummary> findByVehicleNoLike(@Param("vehicleNo") String vehicleNo);
}
