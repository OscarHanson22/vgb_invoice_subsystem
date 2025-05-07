package com.vgb;

import java.util.Optional;

public class Result<T, E> {
	private Optional<T> ok;
	private Optional<E> err;
	
	public Result() {
		this.ok = Optional.empty();
		this.err = Optional.empty();
	}
	
	public Result<T, E> Ok(T ok) {
		if (isErr()) {
			throw new RuntimeException("Result already initialized as an Err variant.");
		}
		
		this.ok = Optional.of(ok);
		return this;
	}
	
	public Result<T, E> Err(E err) {
		if (isOk()) {
			throw new RuntimeException("Result already initialized as an Ok variant.");
		}
		
		this.err = Optional.of(err);
		return this;
	}
	
//	private Result(Optional<T> ok, Optional<E> err) {
//		this.ok = ok;
//		this.err = err;
//	}
//	
////	public Result<T, E> Ok(T ok) {
////		return new Result<>(Optional.of(ok), Optional.empty());
////	}
//	
//	public Result<T, E> Err(E err) {
//		return new Result<>(Optional.empty(), Optional.of(err));
//	}
	
	public boolean isOk() {
		return ok.isPresent();
	}
	
	public Optional<T> ok() {
		return ok;
	}
	
	public boolean isErr() {
		return err.isPresent();
	}
	
	public Optional<E> err() {
		return err;
	}
}
