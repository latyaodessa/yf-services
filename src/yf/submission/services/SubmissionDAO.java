package yf.submission.services;

import yf.submission.entities.Submission;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

public class SubmissionDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Submission persistNewSubmission(final Submission submission) {
        em.persist(submission);
        return submission;
    }

    @Transactional
    public Submission updateSubmission(final Submission submission) {
        em.merge(submission);
        em.flush();
        return submission;
    }


    public Submission findSavedPublicationById(final Long id) {
        return em.find(Submission.class, id);
    }


    public Submission geSubmissionByUUid(final String uuid, final Long userId) {
        TypedQuery<Submission> query = em.createNamedQuery(Submission.QUERY_GET_SUBMISSION_BY_UUID_USERID, Submission.class)
                .setParameter("uuid", uuid)
                .setParameter("userId", userId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Submission> getSubmissionsByUserId(final Long userId, final int offset, final int limit) {
        TypedQuery<Submission> query = em.createNamedQuery(Submission.QUERY_GET_SUBMISSIONS_BY_USERID, Submission.class)
                .setParameter("userId", userId)
                .setFirstResult(offset)
                .setMaxResults(limit);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}
