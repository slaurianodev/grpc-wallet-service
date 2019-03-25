package br.com.srg.persistence.dao;

import br.com.srg.persistence.model.Users;
import br.com.srg.persistence.model.Wallets;
import br.com.srg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

public class WalletsDAO implements IGenericDAO<Wallets> {

    private Session session;

    @Override
    public void initDAOSession() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public Wallets getById(Integer id) {
        return this.session.get(Wallets.class,id);
    }

    @Override
    public void saveOrUpdate(Wallets wallet) {
        session.getTransaction().begin();
        session.saveOrUpdate(wallet);
        session.getTransaction().commit();
    }

    @Override
    public void closeSession() {
        getSession().close();
    }


    public Wallets getByUserAndCurrency(Map<String, Object> params) {
        Query query = session.createQuery("from Wallets w where user.userId =:uId and currency.currencyId =:cId");
        query.setParameter("uId",params.get("userId"));
        query.setParameter("cId",params.get("currencyId"));

        List<Wallets> result = query.list();

        if(result.size() > 0) {
            return result.get(0);
        }else{
            return null;
        }
    }

    public List<Wallets> listByUser(Users user){
        Query query = session.createQuery("from Wallets w where user.userId =:uId");
        query.setParameter("uId",user.getUserId());

        return query.list();
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
