/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.service;

import gr.bc.api.dao.interfaces.IBusinessCardDao;
import gr.bc.api.entity.BusinessCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class BusinessCardService {
    
    @Autowired
    @Qualifier("MySQLBusinessCard")
    private IBusinessCardDao businessCardDao;

    public BusinessCard createBusinessCard(BusinessCard businessCard) {
        return businessCardDao.createBusinessCard(businessCard);
    }
    
    public BusinessCard getBusinessCardByUserId(long userId) {
        return businessCardDao.getBusinessCardByUserId(userId);
    }
    
    public BusinessCard getBusinessCardById(long businessCardId) {
        return businessCardDao.getBusinessCardById(businessCardId);
    }
    
    public boolean isBusinessCardExist(long id) {
        return businessCardDao.isBusinessCardExist(id);
    }
    
}