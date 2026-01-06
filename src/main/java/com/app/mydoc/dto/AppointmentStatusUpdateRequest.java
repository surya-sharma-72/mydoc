package com.app.mydoc.dto;

import com.app.mydoc.entity.AppointmentStatus;
import lombok.Data;

@Data
public class AppointmentStatusUpdateRequest {
    private AppointmentStatus status;
}
