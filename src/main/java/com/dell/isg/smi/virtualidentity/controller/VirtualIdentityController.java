/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.virtualidentity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dell.isg.smi.commons.elm.model.PagedResult;
import com.dell.isg.smi.virtualidentity.model.AssignIdentities;
import com.dell.isg.smi.virtualidentity.model.ReserveIdentities;
import com.dell.isg.smi.virtualidentity.model.VirtualIdentityResponse;

@RestController
@Api(value = "/api/1.0/virtualIdentities", description = "/api/1.0/virtualIdentities")
@RequestMapping("/api/1.0/virtualIdentities")
public interface VirtualIdentityController {

    String PARAMETER_NAME_OFFSET = "offset";
    String PARAMETER_NAME_LIMIT = "limit";
    String DEFAULT_OFFSET = "0";
    String DEFAULT_LIMIT = "10";
    String PATH_URI_ALL_VIRTUAL_IDENTITIES = "/virtualIdentities";
    String PARAMETER_NAME_TYPE = "type";
    String PARAMETER_NAME_VIRTUAL_IDENTITY_ID = "virtualIdentityId";
    String PATH_URI_VIRTUAL_IDENTITY_BY_ID = "/{virtualIdentityId}";
    String PARAMETER_NAME_USAGE_ID = "usageId";

    String ROLE_READ = "ROLE_READ";
    String ROLE_WRITE = "ROLE_WRITE";


    /**
     * Get All Virtual Identities
     * 
     * @param type (string) optional filter query param, allowable values are MAC, IQN, WWNN, WWPN.
     * @param usageId (string) optional filter query param, an identifier for the resource using the virtual identity
     * @param offset (int) pagination record to begin at.
     * @param limit (int) number of records to return per page.
     * @return PagedResult a paged result container that has a list of virtual identities and pagination data
     */
    @ApiOperation(value = "Get All Virtual Identities", nickname = "Get All Virtual Identities", notes = "Gets all virtual identites.  Query Prameter Filter options by type and/or Usage ID are available, as well as pagination offset and limit.", response = PagedResult.class)
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "query", name = "usageId", dataType = "string"), @ApiImplicitParam(paramType = "query", name = "type", dataType = "string", value = "", allowableValues = "MAC,IQN,WWNN,WWPN") })
    @RolesAllowed({ ROLE_READ })
    public abstract PagedResult getAllVirtualIdentitiesByTypeAndOrByUsageId(@RequestParam(value = PARAMETER_NAME_TYPE, defaultValue = "") String type, @RequestParam(value = PARAMETER_NAME_USAGE_ID, defaultValue = "") String usageId, @RequestParam(value = PARAMETER_NAME_OFFSET, defaultValue = DEFAULT_OFFSET) int offset, @RequestParam(value = PARAMETER_NAME_LIMIT, defaultValue = DEFAULT_LIMIT) int limit);


    /**
     * Get Virtual Identity by Id
     * 
     * @param virtualIdentityId (long) Required. the serial identifier assigned to the virtual identifier
     * @return (VirtualIdentityResponse) a virtual identity response object.
     */
    @ApiOperation(value = "Get Virtual Identity by Id", notes = "Get Virtual Identity by Id")
    @RequestMapping(value = PATH_URI_VIRTUAL_IDENTITY_BY_ID, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({ ROLE_READ })
    public abstract VirtualIdentityResponse getVirtualIdentityById(@PathVariable(PARAMETER_NAME_VIRTUAL_IDENTITY_ID) long virtualIdentityId);


    /**
     * Reserve Virtual Identities
     * 
     * @param reserveIdentities (ReserveIdentities) object containing the type, quantity requested, and usageId to assign
     * @return (List<String>) list of virtual Identity string values
     */
    @ApiOperation(value = "Reserve Virtual Identities", notes = "Reserve Virtual Identities")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @RolesAllowed({ ROLE_WRITE })
    @ResponseStatus(HttpStatus.CREATED)
    public abstract List<String> reserveVirtualIdentities(@RequestBody ReserveIdentities reserveIdentities);


    /**
     * Assign Virtual Identities
     * 
     * @param assignIdentities (AssignIdentities)
     */
    @ApiOperation(value = "Assign Virtual Identities", notes = "Assign Virtual Identities")
    @RolesAllowed({ ROLE_WRITE })
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public abstract void assignVirtualIdentities(@RequestBody AssignIdentities assignIdentities);


    /**
     * Release Virtual Identity by ID
     * 
     * @param virtualIdentityId (long) serial identifier assigned to the virtual identifier.
     */
    @ApiOperation(value = "Release Virtual Identity by ID", notes = "Release Virtual Identity by ID")
    @RolesAllowed({ ROLE_WRITE })
    @RequestMapping(value = "/{virtualIdentityId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public abstract void releaseVirtualIdentityById(@PathVariable(PARAMETER_NAME_VIRTUAL_IDENTITY_ID) long virtualIdentityId);


    /**
     * Release Virtual Identities by Usage Id
     * 
     * @param usageId (string) Usage ID is an identifier for the consuming resource
     */
    @ApiOperation(value = "Release Virtual Identities by Usage Id", notes = "Release Virtual Identities by Usage Id")
    @RolesAllowed({ ROLE_WRITE })
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public abstract void releaseVirtualIdentityByUsageId(@RequestParam(PARAMETER_NAME_USAGE_ID) String usageId);

}
