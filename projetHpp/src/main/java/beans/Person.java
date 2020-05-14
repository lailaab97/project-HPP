package beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Person represents a person's informations
 * 
 * @author Team
 * 
 */
public class Person implements Serializable { 
  

	private static final long serialVersionUID = 1L;
    /**
	 * person_id: the person's unique identifier
	 */
	private int person_id;
	
    /**
	 * person_surname: the person's firstname
	 */
	private String person_surname;
	
    /**
	 * person_family_name: the person's lasttname
	 */
	private String person_family_name;
	
    /**
	 * person_date_of_birth: the person's date of birth
	 */
	private Date person_date_of_birth;
	
    /**
	 * diagnosed_ts: the time in which the person was diagnosed positive for the virus
	 */
	private int diagnosed_ts;
	
    /**
	 * contaminated_by: the identifier of the contaminer
	 */
	private int contaminated_by;
	
    /**
	 * contaminated_reason: the details of the potential reason of contamination
	 */
	private String contaminated_reason;
	
    /**
	 * country: the person's country
	 */
	private String country;
    /**
	 * id: the person's score
	 */
	private int score;
	
	
	
    /**
	 * Default constructor
	 * @param
	 * @return Person instance
	 */
	public Person() {
		super();
	}

    /**
	 * Global constructor
	 * @param all attributes
	 * @return Person instance
	 */
	public Person(int person_id, String person_surname, String person_family_name, Date person_date_of_birth,
			int diagnosed_ts, int contaminated_by, String contaminated_reason, String country, int score) {
		super();
		this.person_id = person_id;
		this.person_surname = person_surname;
		this.person_family_name = person_family_name;
		this.person_date_of_birth = person_date_of_birth;
		this.diagnosed_ts = diagnosed_ts;
		this.contaminated_by = contaminated_by;
		this.contaminated_reason = contaminated_reason;
		this.country = country;
		this.score = score;
	}

	
    /**
	 * Constructor
	 * @param 
	 * @return Person instance
	 */
	public Person(int person_id, int diagnosed_ts, int contaminated_by, String country, int score) {
		super();
		this.person_id = person_id;
		this.diagnosed_ts = diagnosed_ts;
		this.contaminated_by = contaminated_by;
		this.country = country;
		this.score = score;
	}

	
    /**
	 * Getters & Setters
	 * 
	 */
	
	public int getPerson_id() {
		return person_id;
	}

	public void setPerson_id(int person_id) {
		this.person_id = person_id;
	}

	public String getPerson_surname() {
		return person_surname;
	}

	public void setPerson_surname(String person_surname) {
		this.person_surname = person_surname;
	}

	public String getPerson_family_name() {
		return person_family_name;
	}

	public void setPerson_family_name(String person_family_name) {
		this.person_family_name = person_family_name;
	}

	public Date getPerson_date_of_birth() {
		return person_date_of_birth;
	}

	public void setPerson_date_of_birth(Date person_date_of_birth) {
		this.person_date_of_birth = person_date_of_birth;
	}

	public int getDiagnosed_ts() {
		return diagnosed_ts;
	}

	public void setDiagnosed_ts(int diagnosed_ts) {
		this.diagnosed_ts = diagnosed_ts;
	}

	public int getContaminated_by() {
		return contaminated_by;
	}

	public void setContaminated_by(int contaminated_by) {
		this.contaminated_by = contaminated_by;
	}

	public String getContaminated_reason() {
		return contaminated_reason;
	}

	public void setContaminated_reason(String contaminated_reason) {
		this.contaminated_reason = contaminated_reason;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contaminated_by;
		result = prime * result + ((contaminated_reason == null) ? 0 : contaminated_reason.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + diagnosed_ts;
		result = prime * result + ((person_date_of_birth == null) ? 0 : person_date_of_birth.hashCode());
		result = prime * result + ((person_family_name == null) ? 0 : person_family_name.hashCode());
		result = prime * result + person_id;
		result = prime * result + ((person_surname == null) ? 0 : person_surname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (contaminated_by != other.contaminated_by)
			return false;
		if (contaminated_reason == null) {
			if (other.contaminated_reason != null)
				return false;
		} else if (!contaminated_reason.equals(other.contaminated_reason))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (diagnosed_ts != other.diagnosed_ts)
			return false;
		if (person_date_of_birth == null) {
			if (other.person_date_of_birth != null)
				return false;
		} else if (!person_date_of_birth.equals(other.person_date_of_birth))
			return false;
		if (person_family_name == null) {
			if (other.person_family_name != null)
				return false;
		} else if (!person_family_name.equals(other.person_family_name))
			return false;
		if (person_id != other.person_id)
			return false;
		if (person_surname == null) {
			if (other.person_surname != null)
				return false;
		} else if (!person_surname.equals(other.person_surname))
			return false;
		return true;
	}
	
	





}
