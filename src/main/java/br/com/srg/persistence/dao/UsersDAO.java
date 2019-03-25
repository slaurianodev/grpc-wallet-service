package br.com.srg.persistence.dao;

import br.com.srg.persistence.model.Users;
import br.com.srg.util.HibernateUtil;
import org.hibernate.Session;

public class UsersDAO implements IGenericDAO<Users> {

    private Session session;

    @Override
    public void initDAOSession() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public Users getById(Integer id) {
        return this.session.get(Users.class,id);
    }

    @Override
    public void saveOrUpdate(Users user) {
        session.getTransaction().begin();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
    }

    public Session getSession() {
        return session;
    }

    @Override
    public void closeSession() {
        getSession().close();
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
