package com.projoker.joker_studio.controller;

import com.projoker.joker_studio.dto.AgendaDto;
import com.projoker.joker_studio.model.Agenda;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.agenda.IAgendaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/agenda")
public class AgendaController {
    private final IAgendaService agendaService;
    private final ModelMapper modelMapper;

    @GetMapping("/get/all/agenda")
    public ResponseEntity<ApiResponse> getAllAgenda(){
        try {
            List<Agenda> agendas=agendaService.getAllAgenda();
            List<AgendaDto> agendaDto=agendas.stream().map(item-> modelMapper.map(item, AgendaDto.class)).toList();
            return ResponseEntity.ok(new ApiResponse("Agenda Retrived Successfully!",agendaDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Retrival of agenda failed.",e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createAgenda(){
        try {
            Agenda agenda=agendaService.createAgenda();
            AgendaDto agendaDto=modelMapper.map(agenda,AgendaDto.class);
            return ResponseEntity.ok(new ApiResponse("Agenda created successfully",agendaDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Creation of agenda failed.",e.getMessage()));
        }
    }

    @PostMapping("/add/accessories/{agendaId}")
    public ResponseEntity<ApiResponse> addAccessories(@PathVariable Long agendaId,
                                                      @RequestParam Long accessoriesId,
                                                      @RequestParam int quantity,
                                                      @RequestParam String event){
        try {
            agendaService.addAccessories(agendaId,accessoriesId,quantity,event);
            return ResponseEntity.ok(new ApiResponse("Accessories added successfully.",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Creation of agenda failed.",e.getMessage()));
        }
    }

    @GetMapping("/get/id/{agendaId}")
    public ResponseEntity<ApiResponse> getAgendaById(@PathVariable Long agendaId){
        try {
            Agenda agenda=agendaService.getAgendaById(agendaId);
            AgendaDto agendaDto=modelMapper.map(agenda,AgendaDto.class);
            return ResponseEntity.ok(new ApiResponse("Agenda retrived.",agendaDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Retrival    of agenda failed.",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{agendaId}")
    public ResponseEntity<ApiResponse> deleteAgendaById(@PathVariable Long agendaId){
        try {
            agendaService.deleteAgendaById(agendaId);
            return ResponseEntity.ok(new ApiResponse("Agenda deleted successfully by id.",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Creation of agenda failed.",e.getMessage()));
        }
    }

    @PutMapping("/update/{agendaId}")
    public ResponseEntity<ApiResponse> updateAgendaDetailsById(@PathVariable Long agendaId,
                                                               @RequestParam Long agendaDetailsId,
                                                               @RequestParam int quantity){
        try {
            agendaService.updateAgendaDetails(agendaId,agendaDetailsId,quantity);
            return ResponseEntity.ok(new ApiResponse("Agenda details are updated successfully.",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Updation of agenda details are failed.",e.getMessage()));
        }
    }
}
