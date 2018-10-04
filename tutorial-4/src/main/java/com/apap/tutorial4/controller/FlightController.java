package com.apap.tutorial4.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial4.model.FlightModel;
import com.apap.tutorial4.model.PilotModel;
import com.apap.tutorial4.service.FlightService;
import com.apap.tutorial4.service.PilotService;

@Controller
public class FlightController {
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private PilotService pilotService;
	
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
	private String add (@PathVariable(value = "licenseNumber") String licenseNumber, Model model) {
		FlightModel flight = new FlightModel();
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		flight.setPilot(pilot);
		
		model.addAttribute("flight",  flight);
		return "addFlight";
	}
	
	@RequestMapping(value = "/flight/add", method = RequestMethod.POST)
	private String addFlightSubmit(@ModelAttribute FlightModel flight) {
		flightService.addFlight(flight);
		return "add";
	}
	
	
	
	@RequestMapping(value = "/flight/delete/{id}", method = RequestMethod.GET)
	private String deleteFlight(@PathVariable(value = "id") Long id) {
		flightService.deleteFlight(id);
		return "deleteFlight";
	}
	
	@RequestMapping(value = "/flight/update/{id}", method = RequestMethod.GET)
	private String updateFlight(@PathVariable(value = "id") Long id, Model model) {
		FlightModel currFlight = flightService.getFlight(id);
		System.out.println(currFlight.getId());
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(currFlight.getPilot().getLicenseNumber());
		currFlight.setPilot(pilot);
		model.addAttribute("currFlight", currFlight);
		return "updateFlight";
	}
	
	@RequestMapping(value = "flight/update", method = RequestMethod.POST)
	private String updateFlightSubmit(@ModelAttribute FlightModel flight) {
		System.out.println(flight.getId());
		flightService.updateFlight(flight, flight.getId());
		return "flightUpdated";
	}
	
	@RequestMapping(value = "flight/view")
	private String viewFlight(@RequestParam("flightNumber") String flightNumber, Model model) {
		List <FlightModel> archiveToGo = new ArrayList();
		List <FlightModel> archive = flightService.getFlightList();
		int i =0;
		for (FlightModel search : archive) {
			if (search.getFlightNumber().equalsIgnoreCase(flightNumber)) {
				archiveToGo.add(search);
				i++;
			}
			
		}
		
		if (i == 0) {
			return "error";
		}
		model.addAttribute("flight_list", archiveToGo);
		return "view-flight";
	}
}
