/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.virtualidentity.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.dell.isg.smi.commons.elm.model.PagedResult;
import com.dell.isg.smi.virtualidentity.exception.EnumErrorCode;
import com.dell.isg.smi.virtualidentity.exception.BadRequestException;
import com.dell.isg.smi.virtualidentity.model.AssignIdentities;
import com.dell.isg.smi.virtualidentity.model.ReserveIdentities;
import com.dell.isg.smi.virtualidentity.model.VirtualIdentityResponse;
import com.dell.isg.smi.virtualidentity.service.IoIdentityManager;

@Component
public class VirtualIdentityControllerImpl implements VirtualIdentityController {

    private final Logger logger = LoggerFactory.getLogger(VirtualIdentityControllerImpl.class.getName());

    @Autowired
    private IoIdentityManager ioIdentityManager;


    /*
     * (non-Javadoc)
     * 
     * @see com.dell.isg.smi.virtualidentity.controller.VirtualIdentityController#getAllVirtualIdentitiesByTypeAndOrByUsageId(java.lang.String, java.lang.String, int, int)
     */
    @Override
    public PagedResult getAllVirtualIdentitiesByTypeAndOrByUsageId(@RequestParam(value = PARAMETER_NAME_TYPE, defaultValue = "") String type, @RequestParam(value = PARAMETER_NAME_USAGE_ID, defaultValue = "") String usageId, @RequestParam(value = PARAMETER_NAME_OFFSET, defaultValue = DEFAULT_OFFSET) int offset, @RequestParam(value = PARAMETER_NAME_LIMIT, defaultValue = DEFAULT_LIMIT) int limit) {
        logger.trace("Entering getAllVirtualIdentitiesByTypeAndOrByUsageId( {}, {}, {}, {} )", type, usageId, offset, limit);
        if (StringUtils.isBlank(usageId) && (type == "")) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorCode(EnumErrorCode.IOIDENTITY_INVALID_INPUT);
            throw badRequestException;
        }
        PagedResult virtualIdentities = ioIdentityManager.getAllVirtualIdentitiesByFilter(type, usageId, offset, limit);
        logger.trace("Exiting getAllVirtualIdentitiesByTypeAndOrByUsageId( {}, {}, {}, {} )", type, usageId, offset, limit);
        return virtualIdentities;
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.dell.isg.smi.virtualidentity.controller.VirtualIdentityController#getVirtualIdentityById(long)
     */
    @Override
    public VirtualIdentityResponse getVirtualIdentityById(@PathVariable(PARAMETER_NAME_VIRTUAL_IDENTITY_ID) long virtualIdentityId) {
        logger.trace("Entering getVirtualIdentityById( {} )", virtualIdentityId);
        VirtualIdentityResponse virtualIdentityResponse = ioIdentityManager.getVirtualIdentityById(virtualIdentityId);
        logger.trace("Exiting getVirtualIdentityById( {} )", virtualIdentityId);
        return virtualIdentityResponse;
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.dell.isg.smi.virtualidentity.controller.VirtualIdentityController#reserveVirtualIdentities(com.dell.isg.smi.virtualidentity.model.ReserveIdentities)
     */
    @Override
    public List<String> reserveVirtualIdentities(@RequestBody ReserveIdentities reserveIdentities) {
        logger.trace("Entering reserveVirtualIdentities");
        List<String> ioIdentityValues = ioIdentityManager.reserveIdentities(reserveIdentities);
        logger.trace("Exiting reserveVirtualIdentities");
        return ioIdentityValues;

    }


    /*
     * (non-Javadoc)
     * 
     * @see com.dell.isg.smi.virtualidentity.controller.VirtualIdentityController#assignVirtualIdentities(com.dell.isg.smi.virtualidentity.model.AssignIdentities)
     */
    @Override
    public void assignVirtualIdentities(@RequestBody AssignIdentities assignIdentities) {
        logger.trace("Entering assignVirtualIdentities");
        ioIdentityManager.assignIdentities(assignIdentities);
        logger.trace("Exiting assignVirtualIdentities");
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.dell.isg.smi.virtualidentity.controller.VirtualIdentityController#releaseVirtualIdentityById(long)
     */
    @Override
    public void releaseVirtualIdentityById(@PathVariable(PARAMETER_NAME_VIRTUAL_IDENTITY_ID) long virtualIdentityId) {
        logger.trace("Entering releaseVirtualIdentityById( {} )", virtualIdentityId);
        if (virtualIdentityId <= 0) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorCode(EnumErrorCode.IOIDENTITY_INVALID_INPUT);
            throw badRequestException;
        }
        ioIdentityManager.releaseIdentitiesByVirtualIdentityId(virtualIdentityId);
        logger.trace("Exiting releaseVirtualIdentityById( {} )", virtualIdentityId);
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.dell.isg.smi.virtualidentity.controller.VirtualIdentityController#releaseVirtualIdentityByUsageId(java.lang.String)
     */
    @Override
    public void releaseVirtualIdentityByUsageId(@RequestParam(PARAMETER_NAME_USAGE_ID) String usageId) {
        logger.trace("Entering releaseVirtualIdentityByUsageGuid( {} )", usageId);
        if (StringUtils.isBlank(usageId)) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorCode(EnumErrorCode.IOIDENTITY_INVALID_INPUT);
            throw badRequestException;
        }
        ioIdentityManager.releaseIdentitiesByUsageGuid(usageId);
        logger.trace("Exiting releaseVirtualIdentityByUsageGuid( {} )", usageId);
    }

}
