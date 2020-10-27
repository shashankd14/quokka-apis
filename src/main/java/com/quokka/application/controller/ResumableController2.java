package com.quokka.application.controller;

import java.io.IOException;
import java.io.InputStream;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.InputStreamContent;

public class ResumableController2 {
//
//	private String initiateResumableUpload() throws IOException {
//        String URI = "https://storage.googleapis.com/" + "quokka-project-uploads" + "/" + "test445.dollhouse";
//        GenericUrl url = new GenericUrl(URI);
//        HttpRequest req = requestFactory.buildPostRequest(url, new ByteArrayContent("text/plain", new byte[0]));
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("x-goog-resumable", "start");
//        headers.setContentLength((long) 0);
//        headers.setContentType("text/plain");
//        req.setHeaders(headers);
//        req.setReadTimeout((int) 3000);
//        req.setResponseHeaders(headers);
//        HttpResponse resp;
//        try {
//            resp = req.execute();
//        } catch (IOException e) {
//            throw e;
//        }
//        if (resp.getStatusCode() == 201) {
//            String location = resp.getHeaders().getLocation();
//            return location;
//
//        } else {
//            throw new IOException();
//        }
//    }
//	
//	private void writeChunk(final boolean isFinalChunk) throws HttpResponseException, IOException {
//	    System.out.println("Writing chunk number " + Integer.toString(100) + ".");
//
//	    try (InputStream inputStream = new ByteBufInputStream(buffer)) {
//	        int length = Math.min(buffer.readableBytes(), 100);
//	        HttpContent contentsend = new InputStreamContent("text/plain", inputStream);
//
//	        String URI = initiateResumableUpload();
//	        GenericUrl url = new GenericUrl(URI);
//	        HttpRequest req = requestFactory.buildPutRequest(url, contentsend);
//
//	        int offset = chunkCount*DEFAULT_UPLOAD_CHUNK_SIZE;
//	        long limit = offset + length;
//	        HttpHeaders headers = new HttpHeaders();
//	        headers.setContentLength((long) length);
//	        headers.setContentRange("bytes " + (length == 0 ? "*" : offset + "-" + (limit - 1)) + (isFinalChunk ? "/" + limit : "/*"));
//
//	        req.setHeaders(headers);
//
//	        req.setReadTimeout((int) 3000);
//
//	        try {
//	            req.execute();
//	            } 
//	        catch (HttpResponseException e) {
//	                if(e.getMessage().equals("308 Resume Incomplete"))
//	                {
//	                    ++chunkCount;
//	                }
//	                else
//	                {
//	                    throw e;
//	                }
//	            }
//	        catch (Exception e) {
//	            throw e;
//	        }
//	    }
//	}
}
