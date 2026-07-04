package serviciosImpl; 

import java.util.List;
import java.util.Map;
import javax.annotation.Resource; // <-- CORREGIDO: Vuelve a javax para Tomcat 9
import modelo.Usuario;
import servicios.ServicioUsuarios;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import constantesSQL.ConstantesSQL;

import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

@Service
@Transactional
public class ServicioUsuariosImpl implements ServicioUsuarios { 

	// @Resource asigna la bean de la id indicada, en este caso fff
	// a la variable a continuación definida
	@Resource(name = "fff")
	private SessionFactory sessionFactory; 

	@Override
	public void registrarUsuario(Usuario u) {
		sessionFactory.getCurrentSession().save(u);		
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public boolean comprobarEmail(String email) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Usuario.class);
		c.add(Restrictions.eq("email", email));
		if (c.uniqueResult() == null) {
			return false;
		} else {
			return true;
		}
	}

	// Con esta anotacion conseguimos que java deje usar metodos antiguos de hibernate como criteria
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public Usuario obtenerUsuarioPorEmailYpass(String email, String pass) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Usuario.class);
		c.add(Restrictions.eq("email", email));
		c.add(Restrictions.eq("pass", pass));
		return (Usuario) c.uniqueResult();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<Usuario> obtenerUsuarios() {
		return sessionFactory.getCurrentSession().createCriteria(Usuario.class).list();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public Map<String, Object> obtenerUsuarioPorId(int id) {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(ConstantesSQL.SQL_OBTENER_USUARIO_POR_ID);
		query.setParameter("id", id);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return (Map<String, Object>) query.uniqueResult();
	}

	@Override
	public void darBajaUsuario(int id) {
		Usuario u = (Usuario) sessionFactory.getCurrentSession().get(Usuario.class, id);
		if (u != null) {
			u.setAlta(false);
			sessionFactory.getCurrentSession().update(u);
		}
	}

	@Override
	public void darAltaUsuario(int id) {
		Usuario u = (Usuario) sessionFactory.getCurrentSession().get(Usuario.class, id);
		if (u != null) {
			u.setAlta(true);
			sessionFactory.getCurrentSession().update(u);
		}
	}

}
 
	

 