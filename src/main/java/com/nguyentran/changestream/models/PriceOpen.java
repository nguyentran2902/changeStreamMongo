package com.nguyentran.changestream.models;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Document(collection = "priceOpen")
public class PriceOpen {
	
	@Id
	private String _id;
	private List<String> dateAvailable;
	private String tourId ;
	private String currency;
	private Double price;
	
}
