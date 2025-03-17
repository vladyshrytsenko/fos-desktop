package org.example.fosdesktop.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class DrinkDto implements MenuItem {

    private Long id;
    private String name;
    private Float price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

    @Override
    public double getPrice() {
        return price != null ? price.doubleValue() : 0.0;
    }

}
