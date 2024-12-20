/*
 * Copyright © 2013 - 2018 camunda services GmbH and various authors (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.camunda.bpm.example.spring.soap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camunda.bpm.example.spring.soap.v1.BankCustomerServicePortType;
import com.camunda.bpm.example.spring.soap.v1.BankException_Exception;
import com.camunda.bpm.example.spring.soap.v1.BankRequestHeader;
import com.camunda.bpm.example.spring.soap.v1.GetAccountsRequest;
import com.camunda.bpm.example.spring.soap.v1.GetAccountsResponse;

/**
 * 
 * Common service exposing web service. Add logic for throttling, service authentication and such here.
 * 
 */

@Service
public class BankCustomerClientService implements BankCustomerServicePortType {

	private BankCustomerServicePortType port;
	
	@Autowired
	public BankCustomerClientService(BankCustomerServicePortType port) {
		this.port = port;
	}

	@Override
	public GetAccountsResponse getAccounts(GetAccountsRequest parameters, BankRequestHeader bankHeader) throws BankException_Exception {
		return port.getAccounts(parameters, bankHeader);
	}
}

