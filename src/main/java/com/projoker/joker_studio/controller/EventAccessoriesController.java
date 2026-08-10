package com.projoker.joker_studio.controller;

import com.projoker.joker_studio.enums.EventName;
import com.projoker.joker_studio.model.EventAccessories;
import com.projoker.joker_studio.request.AddEventAccessoriesRequest;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.event_accessories.EventAccessoriesService;
import com.projoker.joker_studio.service.event_accessories.IEventAccessoriesService;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/accessories")
@RequiredArgsConstructor
public class EventAccessoriesController {
    private final IEventAccessoriesService eventAccessoriesService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createAccessories(@RequestBody AddEventAccessoriesRequest request){
        HashMap<EventName, BigDecimal> prices=new HashMap<>();
        prices.put(EventName.WEDDING,request.getWedding());
        prices.put(EventName.COLLEGE_FESTIVALS,request.getCollegeFestivals());
        prices.put(EventName.OTHERS,request.getOthers());
        try {
            eventAccessoriesService.createAccessories(request.getName(),request.getDescription(),prices);
            return ResponseEntity.ok(new ApiResponse("Accessories created successfully!",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Creating Accessories is failed.",e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateAccessories(@PathVariable Long id, @RequestBody AddEventAccessoriesRequest request){
        HashMap<EventName, BigDecimal> prices=new HashMap<>();
        prices.put(EventName.WEDDING,request.getWedding());
        prices.put(EventName.COLLEGE_FESTIVALS,request.getCollegeFestivals());
        prices.put(EventName.OTHERS,request.getOthers());
        try {
            eventAccessoriesService.updateAccessories(id,request.getName(),request.getDescription(),prices);
            return ResponseEntity.ok(new ApiResponse("Accessories are updated Successfully.",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Updating Accessories is failed.",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> removeAccessories(@PathVariable Long id){
        try {
            eventAccessoriesService.deleteEventAccessories(id);
            return ResponseEntity.ok(new ApiResponse("Accessory are Deleted Successfully.",null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Deleting Accessories is failed.",e.getMessage()));
        }
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<ApiResponse> getAccesoriesByName(@PathVariable String name){
        try {
            List<EventAccessories> eventAccessories = eventAccessoriesService.getAccessoriesByName(name);
            return ResponseEntity.ok(new ApiResponse("Accesories retrived.",eventAccessories));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Retriving Accessories is failed.",e.getMessage()));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getAccesoriesById(@PathVariable Long id){
        try {
            EventAccessories eventAccessories = this.eventAccessoriesService.getAccessoriesById(id);
            return ResponseEntity.ok(new ApiResponse("Accesories retrived.",eventAccessories));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Retriving Accessories is failed.",e.getMessage()));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllAccesories(){
        try {
            List<EventAccessories> allAccesories=eventAccessoriesService.getAll();
            return ResponseEntity.ok(new ApiResponse("All accesories!",allAccesories));
         }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Retriving Accessories is failed.",e.getMessage()));
        }
    }
}
