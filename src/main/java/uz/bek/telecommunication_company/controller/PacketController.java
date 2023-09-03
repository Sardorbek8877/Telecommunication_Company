package uz.bek.telecommunication_company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bek.telecommunication_company.entity.Packet;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.PacketDto;
import uz.bek.telecommunication_company.payload.PacketDtoForClients;
import uz.bek.telecommunication_company.service.PacketService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/packet")
public class PacketController {

    @Autowired
    PacketService packetService;

    /**
     * ADD PACKET
     * @param packetDto
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasRole('ROLE_MANGER')")
    @PostMapping("/addPacket")
    public HttpEntity<?> addPacket(@RequestBody PacketDto packetDto) {
        ApiResponse apiResponse = packetService.addPacket(packetDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * GET ALL PACKETS
     * @return List<Packet>
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER','ROLE_STAFF')")
    @GetMapping("/getAllPacketsForStaff")
    public HttpEntity<?> getAllPackets() {
        List<Packet> packetList=packetService.getAllPackets();
        return ResponseEntity.ok(packetList);
    }

    /**
     * EDIT PACKET
     * @param packetDto
     * @param id
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasRole('ROLE_MANGER')")
    @PostMapping("/editPacket/{id}")
    public HttpEntity<?> editPacket(@RequestBody PacketDtoForClients packetDto, @PathVariable UUID id) {
        ApiResponse apiResponse = packetService.editPacket(packetDto,id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * DELETE PACKET
     * @param id
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasRole('ROLE_MANGER')")
    @DeleteMapping("/deletePacket/{id}")
    public HttpEntity<?> deletePacket(@PathVariable UUID id){
        ApiResponse apiResponse=packetService.deletePacket(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /**
     * GET PACKET LIST FOR CLIENTS
     * @return List<String>
     */
    @PreAuthorize(value = "hasRole('ROLE_CLIENT')")
    @GetMapping("/getAllPackets")
    public HttpEntity<?> getAllPacketsForClients() {
        List<String> packetList=packetService.getAllPacketsForClients();
        return ResponseEntity.ok(packetList);
    }

    /**
     * GET PACKET INFO
     * @param packetName
     * @return PacketDtoForClients
     */
    @PreAuthorize(value = "hasRole('ROLE_CLIENT')")
    @GetMapping("/getPacketInfo/{packetName}")
    public HttpEntity<?> getPacketInfo(@PathVariable String packetName) {
        PacketDtoForClients packetInfo=packetService.getPacketInfo(packetName);
        return ResponseEntity.ok(packetInfo);
    }
}
