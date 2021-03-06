/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.care_point.other.vtype;

import com.mac.care_point.other.vtype.model.Vtype;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class VtypeService {

    @Autowired
    private VtypeRepository vtypeRepository;

    public List<Vtype> findVehicleType() {
        return vtypeRepository.findAll();
    }
}
