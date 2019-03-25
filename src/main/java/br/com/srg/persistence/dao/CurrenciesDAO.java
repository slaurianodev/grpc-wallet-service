package br.com.srg.persistence.dao;

import br.com.srg.persistence.model.Currencies;
import br.com.srg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CurrenciesDAO implements IGenericDAO<Currencies> {

    private Session session;

    @Override
    public void initDAOSession() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public Currencies getById(Integer id) {
        return this.session.get(Currencies.class,id);
    }

    @Override
    public void saveOrUpdate(Currencies Currencies) {
        session.getTransaction().begin();
        session.saveOrUpdate(Currencies);
        session.getTransaction().commit();
    }

    public Currencies getByCurrencyCode(String currencyCode){
        Query query = session.createQuery("from Currencies where currencyCode = :code");
        query.setParameter("code",currencyCode);

        return (Currencies) query.uniqueResult();
    }

    public List<Currencies> listAll(){
        Query query = session.createQuery("from Currencies");
        return query.list();
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
