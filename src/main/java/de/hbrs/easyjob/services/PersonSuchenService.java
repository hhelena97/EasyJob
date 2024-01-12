package de.hbrs.easyjob.services;

import de.hbrs.easyjob.entities.Student;
import de.hbrs.easyjob.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.hbrs.easyjob.entities.Person;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class PersonSuchenService {

    @Autowired
    private PersonRepository personRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<Person> vollTextSuche(String vollText){
        String query = prepareFullTextSearchQuery(vollText);
        return personRepository.vollTextSuche(query);
    }

    private String prepareFullTextSearchQuery(String searchText){
        return Arrays.stream(searchText.split(" "))
                .map(word -> word + "*")
                .collect(Collectors.joining(" & "));
    }


    public boolean istVollTextSuche(String keyword){
        return keyword.contains(" ") || keyword.length()>=4;
    }

    public List<Person> getAllPersonen(){
        return personRepository.findAll();
    }
    public Person getPersonById(Integer id){
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }
    public List<Person> getPersonenByIds(List<Integer> ids){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> person = cq.from(Person.class);
        cq.select(person).where(person.get("id_Person").in(ids));
        return entityManager.createQuery(cq).getResultList();
    }
}
