package com.example.resumebuilder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.resumebuilder.dto.AuthResponse;
import static com.example.resumebuilder.util.AppConstants.PREMIUM;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplatesService {
       
    private final AuthService authService;
 


    public Map<String, Object> getTemplates(Object principal){
           // get the current profile

            AuthResponse authResponse =  authService.getProfile(principal);

           // get the available templates based on subscription
          
           List<String> availableTemplates;

          Boolean isPremium = PREMIUM.equalsIgnoreCase(authResponse.getSubscriptionPlan());

           
          if(isPremium) {
            availableTemplates = List.of("01", "02", "030");
          }else {
            availableTemplates = List.of("01");
          }
           // add the data into map
          Map<String, Object> restrictions = new HashMap<>();
          restrictions.put("availableTemplates", availableTemplates);
          restrictions.put("allTemplates", List.of("01", "02", "03"));
          restrictions.put("subscriptionPlan" , authResponse.getSubscriptionPlan());
          restrictions.put("isPremium", isPremium);

           // return the result
           return restrictions;
    }
    
}
