package com.testfairy.uploader;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class SelfReturningAnswer implements Answer<Object> {
	@Override
	public Object answer(InvocationOnMock invocation) throws Throwable {
		Object mock = invocation.getMock();
		if( invocation.getMethod().getReturnType().isInstance( mock )){
			return mock;
		}
		else{
			return Mockito.RETURNS_DEFAULTS.answer(invocation);
		}
	}
}