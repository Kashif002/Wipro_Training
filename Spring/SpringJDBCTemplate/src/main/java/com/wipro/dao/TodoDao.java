package com.wipro.dao;


import java.util.List;

import com.wipro.beans.Todo;



public interface TodoDao
{
	public int saveTodo(Todo todo);
	public int updateTodo(Todo todo);
	public int deleteTodo(int id);
	public Todo getTodo(int id);
	public List<Todo> getAllTodos();
	

}