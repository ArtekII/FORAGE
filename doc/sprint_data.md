# Spring Data :

La line : `<jpa:repositories base-package="repository" entity-manager-factory-ref="entityManagerFactory"/>` dans `beans.xml` est la configuration de correspondance entre le repository.

## Ajout des tables client et compte_bancaire dans la base :
```
CREATE TABLE client (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50)
);

CREATE TABLE compte_bancaire (
    id INT PRIMARY KEY AUTO_INCREMENT,
    num_compte VARCHAR(50),
    solde DECIMAL(10, 2),
    client_id INT,
    FOREIGN KEY (client_id) REFERENCES client(id)
);
```

## Repository :
Un repository permet de prendre toutes les methodes d'acces a la base (findById, save, findAll...), ou definir soit meme une fonction a partir de `@Query`

On le definit comme ceci.

```
package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
```

Dans le `extends JpaRepository<Client, Long>` Client est le model qui correspond et Long est le type de l'id. 

## Exemple d'un model en utilisant les annotations JPA : 

### Pour compte_bancaire :

```
package model;

# Importation

@Entity
public class CompteBancaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="num_compte")
    private String numeroCompte;
    private Double solde;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    
    public Client getClient() {
        return client;
    }
    public Long getId() {
        return id;
    }
    public String getNumeroCompte() {
        return numeroCompte;
    }
    public Double getSolde() {
        return solde;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }
} 

```

### Pour client :

```
package model;

# Importation

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<CompteBancaire> comptes = new ArrayList<>();
    
    public List<CompteBancaire> getComptes() {
        return comptes;
    }
    public Long getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public void setComptes(List<CompteBancaire> comptes) {
        this.comptes = comptes;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
}
```

`@Entity` marque une classe comme un entite correspondant a une table dans la base

Pour definir l'id :
``@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)``


Hibernate utilise une strategie appeler : `CamelCaseToUnderscoreNamingStrategy`
en ajoutant dans `persistence.xml` :
```
<property name="hibernate.physical_naming_strategy" 
          value="org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"/>
```
Sans ca, on doit specifier pour chaque attribut 

`@Column(name="column_name")
TypeAttribut attribut`

Pour chaque classe, Hibernate trouve automatiquement la table correspondant, mais si c'est
("Classe:CompteBancaire" -> "Table:compte_bancaire"), Hibernate utilise aussi la strategie.

On utilise `@Table` dans le cas ou le nom de la table et de la classe different .

Si l'attribut s'appelle `numeroCompte` (Camelcase)
Hibernate cherchera automatiquement une colonne nommee `numero_compte` (SnakeCase)

D'ou la precision @Column(name="num_compte").

|Attribut   |Colonne  |Besoin de @Column|
|---          |--- |:-:
|nom         |nom       |non               |
|dateCreation    |date_creation |non               |
|numeroCompte|num_compte|oui               |

`@JoinColumn` permet de faire la jointure sur la colonne "client_id"

`@OneToMany` ainsi que `@ManyToOne` est utiliser dans le cas ou plusieurs entite est relier a une entite.

Exemple :
- Un auteur (One) a ecris plusieurs livres (Many)

Dans notre cas, l'attribut `private List<CompteBancaire> comptes = new ArrayList<>();` prend les compte bancaires qui ont comme client, la valeur du client, et cela est possible a partir de `mappedBy` qui est relier avec le nom de l'attribut dans le model CompteBancaire. Et `cascade = CascadeType.ALL` supprime tous les compte bancaires au cas ou le client est supprimer.

