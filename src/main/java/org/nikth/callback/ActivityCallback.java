package org.nikth.callback;

import java.util.List;
import java.util.Map;

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiException;
import io.swagger.client.model.DetailedActivity;

public class ActivityCallback implements ApiCallback<DetailedActivity> 
{

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(DetailedActivity result, int statusCode, Map<String, List<String>> responseHeaders) 
	{
		System.out.println("got response:"+ result);
	}

	@Override
	public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
		// TODO Auto-generated method stub
		
	}
	
}
