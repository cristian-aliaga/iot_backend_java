package com.sensormanager.iot.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.dto.SensorJSONPackageDTO;
import com.sensormanager.iot.service.SensorDataServiceImp;

@MockitoSettings
@AutoConfigureMockMvc
class SensorDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SensorDataServiceImp sensorDataService;

    @InjectMocks
    private SensorDataController sensorDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sensorDataController).build();
    }

    @Test
    void testGetSensorDataReturnsOk() throws Exception {
        List<SensorDataDTO> mockData = Arrays.asList(new SensorDataDTO(), new SensorDataDTO());
        Mockito.when(sensorDataService.getSensorData(anyList(), anyLong(), anyLong())).thenReturn(mockData);

        mockMvc.perform(get("/api/v1/sensordata")
                .param("sensor_id", "1,2")
                .param("from", "1633036800")
                .param("to", "1633123200"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{},{}]"));
    }

    @Test
    void testGetSensorDataReturnsNoContent() throws Exception {
        Mockito.when(sensorDataService.getSensorData(anyList(), anyLong(), anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/sensordata")
                .param("sensor_id", "1,2")
                .param("from", "1633036800")
                .param("to", "1633123200"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateSensorDataReturnsCreated() throws Exception {
        List<SensorDataDTO> mockData = Arrays.asList(new SensorDataDTO());
        Mockito.when(sensorDataService.createSensorData(any(SensorJSONPackageDTO.class))).thenReturn(mockData);

        mockMvc.perform(post("/api/v1/sensordata")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"someField\":\"someValue\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("[{}]"));
    }

    @Test
    void testCreateSensorDataReturnsBadRequest() throws Exception {
        Mockito.when(sensorDataService.createSensorData(any(SensorJSONPackageDTO.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/api/v1/sensordata")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"someField\":\"someValue\"}"))
                .andExpect(status().isBadRequest());
    }
}
