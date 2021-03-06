/*
 * All GTAS code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package gov.gtas.controller;

import gov.gtas.model.Case;
import gov.gtas.services.CaseDispositionService;
import gov.gtas.services.dto.CasePageDto;
import gov.gtas.services.dto.CaseRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CaseDispositionController {

    @Autowired
    private CaseDispositionService caseDispositionService;

    @RequestMapping(value = "/getAllCaseDispositions", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    CasePageDto getAll(@RequestBody CaseRequestDto request, HttpServletRequest hsr) {
        hsr.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT",
                SecurityContextHolder.getContext());
        return caseDispositionService.findAll(request);
    }

    //getOneHistDisp
    @RequestMapping(method = RequestMethod.POST, value = "/getOneHistDisp",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Case getOneHistDisp(@RequestBody CaseRequestDto request, HttpServletRequest hsr)
            throws ParseException {
        HashMap _tempMap = new HashMap();

        return caseDispositionService.findHitsDispositionByCriteria(request);
    }


    //getHistDispComments
    @RequestMapping(method = RequestMethod.GET, value = "/getHistDispComments")
    public Map<String, Object> getHistDispComments(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate)
            throws ParseException {
        HashMap _tempMap = new HashMap();

        return _tempMap;
    }

    //updateHistDisp
    @RequestMapping(method = RequestMethod.POST, value = "/updateHistDisp")
    public
    @ResponseBody
    Case updateHistDisp(@RequestBody CaseRequestDto request, HttpServletRequest hsr) {
        Case aCase = new Case();
        try {
            aCase = caseDispositionService.addCaseComments(request.getFlightId(), request.getPaxId(),
                                                            request.getHitId(), request.getCaseComments(),
                                                                request.getStatus(), request.getValidHit());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return aCase;
    }

    //updateCase
    @RequestMapping(method = RequestMethod.POST, value = "/updateCase")
    public Map<String, Object> updateCase(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate)
            throws ParseException {
        HashMap _tempMap = new HashMap();

        return _tempMap;
    }

}
