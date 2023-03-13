package jb.CouponsBack.controllers;

import jb.CouponsBack.beans.UserDetails;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


public abstract class ClientController {
   ;



  public abstract ResponseEntity<?> login(@RequestBody UserDetails userDetails);


}
