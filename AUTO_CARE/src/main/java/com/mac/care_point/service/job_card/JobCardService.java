/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.care_point.service.job_card;

import com.mac.care_point.service.job_card.model.Category;
import com.mac.care_point.service.job_card.model.CategoryDetail;
import com.mac.care_point.service.job_card.model.Customer;
import com.mac.care_point.service.job_card.model.Description;
import com.mac.care_point.service.job_card.model.JobSummary;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class JobCardService {

    @Autowired
    private JobCardRepository jobCardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Autowired
    private CategoryDetailRepository categoryDetailRepository;

    public List<JobSummary> findByVehicleNo(String vehicleNo) {
        //default user input ex:KR8755
        String vehicleNo1 = vehicleNo;
        //add separator at 2nd index, ex:KR-8755
        String vehicleNo2 = vehicleNo.length() > 2 ? vehicleNo.substring(0, 2) + "-" + vehicleNo.substring(2) : vehicleNo;
        //add separator at 3r index, ex:KR8-755, 250-8466
        String vehicleNo3 = vehicleNo.length() > 3 ? vehicleNo.substring(0, 3) + "-" + vehicleNo.substring(3) : vehicleNo;

        return jobCardRepository.findByVehicleNoLikeLimit10(vehicleNo1, vehicleNo2, vehicleNo3);
    }

    @Transactional
    public JobSummary saveJobSummary(JobSummary jobSummary) {
        //new customer
        Date date = new Date();
        System.out.println(jobSummary.getJobNo());
        if (null == jobSummary.getJobNo()) {
            System.out.println(jobSummary.getCustomer());
            if (jobSummary.getCustomer().getCustomerNo() == null) {
                Integer maxCustomerNO = customerRepository.maxCustomerNumber();
                maxCustomerNO = maxCustomerNO == null ? 1 : maxCustomerNO;
                jobSummary.getCustomer().setCustomerNo(String.format("%06d", maxCustomerNO + 1));
                System.out.println("1");
            }
            jobSummary.setCustomer(customerRepository.save(jobSummary.getCustomer()));
            System.out.println("2");
            System.out.println(jobSummary.getCustomer());

            Integer maxJobNo = jobCardRepository.maxJobCardNo();
            maxJobNo = maxJobNo == null ? 1 : maxJobNo;

            jobSummary.setJobNo(String.format("%06d", maxJobNo + 1));
            jobSummary.setJobC("N");

            jobSummary.setJobDate(date);

            //convert string - time
            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss aa");
            jobSummary.setInTime(format.format(new Date()));

        } else {
            if (jobSummary.getJobC().equals("Y")) {
                if (jobSummary.getCustomer().getCustomerNo() == null) {
                    Integer maxCustomerNO = customerRepository.maxCustomerNumber();
                    maxCustomerNO = maxCustomerNO == null ? 1 : maxCustomerNO;
                    jobSummary.getCustomer().setCustomerNo(String.format("%06d", maxCustomerNO + 1));
                }
                jobSummary.setCustomer(customerRepository.save(jobSummary.getCustomer()));

                Integer maxJobNo = jobCardRepository.maxJobCardNo();
                System.out.println(maxJobNo);
                maxJobNo = maxJobNo == null ? 1 : maxJobNo;

                jobSummary.setJobNo(String.format("%06d", maxJobNo + 1));
                System.out.println(jobSummary.getJobNo());
                jobSummary.setJobC("N");

                jobSummary.setJobDate(date);

                //convert string - time
                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss aa");
                jobSummary.setInTime(format.format(new Date()));

            }
        }
        return jobCardRepository.save(jobSummary);
    }

    public List<JobSummary> findAllJob() {
        return jobCardRepository.findAll();
    }

    public List<Customer> findAllCustomer() {
        return customerRepository.findAll();
    }

    public Customer findCustomerByNic(String nic) {
        return customerRepository.findByNic(nic);
    }

    public Customer findCustomerBymobileNo(String mobileNo) {
        return customerRepository.findByMobileNo(mobileNo);
    }

    public List<Category> findAllCategorys() {
        return categoryRepository.findAll();
    }

    public List<Description> findAllDescription() {
        return descriptionRepository.findAll();
    }

    public List<CategoryDetail> findAllCategoryDetail(String jobNo) {
        return categoryDetailRepository.findByJobNo(jobNo);
    }

    @Transactional
    public CategoryDetail saveCategoryDetails(CategoryDetail categoryDetail, String jobNo) {
        categoryDetail.setJobNo(jobNo);
        Description description = new Description();
        description.setItemDes(categoryDetail.getItemDescription());

        Category category = new Category();
        category.setCategory(categoryDetail.getCategory());

//        List<Category> CategoryList = categoryRepository.findByCategory(categoryDetail.getCategory());
//        if (CategoryList.isEmpty()){
//            categoryRepository.save(category);
//        }
        List<Description> descList = descriptionRepository.findByItemDes(categoryDetail.getItemDescription());
        if (descList.isEmpty()) {
            descriptionRepository.save(description);
        }

        return categoryDetailRepository.save(categoryDetail);
    }

    public void deleteCategoryDetails(Integer indexNo) {
        categoryDetailRepository.delete(indexNo);
    }

    public List<Description> findByItemDescription(String description) {
        return descriptionRepository.findByItemDes(description);
    }

//    public Description saveDescription(Description description) {
//        return descriptionRepository.save(description);
//    }
    public Customer saveCustomer(Customer customer) {
        Integer maxCustomerNO = customerRepository.maxCustomerNumber();
        maxCustomerNO = maxCustomerNO == null ? 1 : maxCustomerNO;
        customer.setCustomerNo(String.format("%06d", maxCustomerNO + 1));
        return customerRepository.save(customer);
    }
}
