package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrgsLogoFilesRequest {

   private File files;
   
   
   @NoArgsConstructor
   @AllArgsConstructor
   @Data
   public static class File{
       @JsonProperty("logo_file")
       private String logeFile;
   }
}
