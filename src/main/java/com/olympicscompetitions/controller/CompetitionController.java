package com.olympicscompetitions.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olympicscompetitions.entity.Competition;
import com.olympicscompetitions.exception.ExceptionDurationOfCompetition;
import com.olympicscompetitions.exception.ExceptionSameCountry;
import com.olympicscompetitions.exception.ExceptionTimeConflict;
import com.olympicscompetitions.messages.ResponseMessage;
import com.olympicscompetitions.service.CompetitionService;

@RestController
@RequestMapping("/competitions")
public class CompetitionController {

	@Autowired
	private CompetitionService competitionService;

//	 @RequestMapping(method = RequestMethod.GET)
//	 public Collection<Competition> getAllCompetitions(){
//	 return competitionService.getAllCompetitions();
//	 }

	@RequestMapping(method = RequestMethod.GET)
	public Collection<Competition> getCompetitionByModality(@RequestParam(value = "modality", required=false) String modality,
			HttpServletRequest req) {
		return competitionService.getCompetitionByModality(modality);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Competition getCompetitionById(@PathVariable("id") int id) {
		return competitionService.getCompetitionById(id);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public void removeCompetitionById(@PathVariable("id") int id) {
		competitionService.removeCompetitionById(id);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseMessage updateCompetition(@RequestBody Competition competition) {
	try{
		Competition competitionUpdated = competitionService.updateCompetition(competition);
		return new ResponseMessage("Success", "Competition changed successfully");
	} catch (ExceptionTimeConflict e) {
		return new ResponseMessage("Error", "The same date and time is not allowed for the same location and modality");
	} catch (ExceptionSameCountry e) {
		return new ResponseMessage("Error", "Confronted with the same country only is allowed in the semi-finals and finals");
	} catch (ExceptionDurationOfCompetition e) {
		return new ResponseMessage("Error", "Duration of a competition must be at least 30 minutes");
	}
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseMessage insertCompetition(@RequestBody Competition competition) {
		try {
			Competition competitionObject = competitionService.insertCompetition(competition);
			return new ResponseMessage("Success", "Competition inserted successfully");

		} catch (ExceptionTimeConflict e) {
			return new ResponseMessage("Error", "The same date and time is not allowed for the same location and modality");
		} catch (ExceptionSameCountry e) {
			return new ResponseMessage("Error", "Confronted with the same country only is allowed in the semi-finals and finals");
		} catch (ExceptionDurationOfCompetition e) {
			return new ResponseMessage("Error", "Duration of a competition must be at least 30 minutes");
		}
	}

}