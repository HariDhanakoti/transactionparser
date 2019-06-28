package au.mebank.finance.repository;

import au.mebank.finance.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query("select t from Transaction t where t.createdAt >= :startLimit AND t.createdAt <= :endLimit")
    List<Transaction> findTransactionsWithinStartAndEndTime(
            @Param("startLimit") Date startLimit,
            @Param("endLimit") Date endLimit);

    @Query("select t from Transaction t where t.fromAccountId = :accountId OR t.toAccountId = :accountId AND t.createdAt >= :startLimit AND t.createdAt <= :endLimit")
    List<Transaction> findTransactionForTheAccountWithinStartAndEndTime(
            @Param("accountId") String accountId,
            @Param("startLimit") Date startLimit,
            @Param("endLimit") Date endLimit);

    @Query("select t from Transaction t where t.fromAccountId = :accountId")
    List<Transaction> findTransactionsForTheAccount(@Param("accountId") String accountId);
}
