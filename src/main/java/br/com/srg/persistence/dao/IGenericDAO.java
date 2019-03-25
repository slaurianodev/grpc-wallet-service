package br.com.srg.persistence.dao;


import java.io.Serializable;

public interface IGenericDAO<T extends Serializable  > {

    public void initDAOSession();
    public T getById(Integer id);
    public void saveOrUpdate(T object);
    public void closeSession();

}
