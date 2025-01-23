import json
from elasticsearch import Elasticsearch, helpers
from model.model import Address,FamilyMember,JoiningDate,Leaves,Employee,Salary

def load_csv_files():
    employees = []
    family_members = []
    addresses = []
    salarys=[]
    joiningDate=[]
    leave_records=[]


    employee_json_file_path="./data/empjson.json"


    with open(employee_json_file_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
        print("processing employee data")
        for item in data['employees']:
            employee = Employee(
                id=item['id'],
                    empid=item['empid'],
                    empname=item['empname'],
                    role='#'.join(item['role']),
                    tablename='employee'
                )
            employees.append(employee)
        print("processing salary data")
        for item in data["salarys"]:
                salary=Salary(
                    id=item["salaryid"],
                    empid=item["empid"],
                    salary=int(item["amount"]),
                    role='#'.join(item["role"]),
                    tablename='salary'
                )
                salarys.append(salary)

        print("processing joining date data")
        for item in data["joiningDates"]:
                date=JoiningDate(
                    id=item["id"],
                    empid=item["empid"],
                    joinDate=item["joinDate"],
                    tablename='joinDate'
                )
                joiningDate.append(date)
        print("processing leave data")
        for item in data["leaves"]:
                leave=Leaves(
                    id=item["id"],
                    empid=item["empid"],
                    leave_type=item["leave_type"],
                    leave_start_date=item["leave_start_date"],
                    leave_end_date=item["leave_end_date"],
                    number_of_days=item["number_of_days"],
                    leave_status=item["leave_status"],
                    tablename='leaves'
                )
                leave_records.append(leave)
        print("processing address data")
        for item  in data["addresses"]:
                address=Address(
                    id=item["id"],
                    city=item["city"],
                    empid=item["empid"],
                    street=item["street"],
                    state=item["state"],
                    zipcode=item["zipcode"],
                    country=item["country"],
                    contact=item["contact"],
                    tablename='address'
                )
                addresses.append(address)
        print("processing family data")
        for item in data["families"]:
                member=FamilyMember(
                    id=item["id"],
                    empid=item["empid"],
                    relation=item["relation"],
                    name=item["name"],
                    age=item["age"],
                    occupation=item["occupation"],
                    contact=item["contact"],
                    tablename='family'
                )
                family_members.append(member)

    return employees,family_members,addresses,salarys,joiningDate,leave_records




def generate_bulk_data_for_parent_child(employees, family_members, addresses,salarys,joiningDate,leave_records, index_name):
    
    if not es.indices.exists(index=index_name):
        mappings = {
            "mappings": {
                "properties": {
                    "empid": {"type": "keyword"},
                    "empname": {"type": "text"},
                    "emp-join-field": {
                        "type": "join",
                        "relations": {
                            "employee": ["address", "family","salary","joiningDate","leaves"]
                        }
                    }
                }
            }
        }
        es.indices.create(index=index_name, body=mappings)
        print(f"Index {index_name} created successfully.")

    for employee in employees:
    
        yield {
            "_op_type": "index",  
            "_index": index_name,
            "_id": employee.empid,  
            "_source": {
                "empid": employee.empid,
                "empname": employee.empname,
                "role":employee.role,
                "tablename":employee.tablename,
                "emp-join-field": {"name": "employee"}
            }
        }

    print("Processing family data")
    for family_member in family_members:
        yield {
            "_op_type": "index",
            "_index": index_name,
            "_routing": family_member.empid,
            "_id":family_member.id,  
            "_source": {
                "empid": family_member.empid,
                "relation": family_member.relation,
                "name": family_member.name,
                "age": family_member.age,
                "occupation": family_member.occupation,
                "contact": family_member.contact,
                "tablename":family_member.tablename,
                "emp-join-field": {"name": "family", "parent": family_member.empid}
            }
        }

    print("Processing address data")
    for address in addresses:
        yield {
            "_op_type": "index",
            "_index": index_name,
            "_routing": address.empid,
            "_id":address.id,  
            "_source": {
                "empid":address.empid,
                "street": address.street,
                "city": address.city,
                "state": address.state,
                "zipcode": address.zipcode,
                "country": address.country,
                "tablename":address.tablename,
                "emp-join-field": {"name": "address", "parent": address.empid}
            }
        }
    print("Processing salary data")
    try:
        for salary in salarys:
            yield {
                "_op_type": "index",
                "_index": index_name,
                "_routing": salary.empid,
                "_id": salary.id,
                "_source": {
                    "id": salary.id,
                    "empid": salary.empid,
                    "salary": salary.salary,
                    "role": salary.role,
                    "tablename": salary.tablename,
                    "emp-join-field": {"name": "salary", "parent": salary.empid}
                }
            }
    except Exception as e:
        print(f"Error processing salary data: {e}")

    print("Processing join data")
    for joinDate in joiningDate:
        yield{
            "_op_type": "index",
            "_index": index_name,
            "_routing": joinDate.empid,  
            "_id":joinDate.id,
            "_source": {
                "id":joinDate.id,
                "empid": joinDate.empid,
                "joinDate":joinDate.joinDate,
                "tablename":joinDate.tablename,
                "emp-join-field": {"name": "joiningDate", "parent": joinDate.empid}
            }
        }
    print("Processing leave data")
    for leave in leave_records:
        yield {
            "_op_type": "index",  # Operation type for Elasticsearch
            "_index": index_name,  # Index name
            "_routing": leave.empid,
            "_id":leave.id,  # Route by employee ID
            "_source": {
                "id": leave.id,  # Leave ID
                "empid": leave.empid,  # Employee ID
                "leave_type": leave.leave_type,  # Type of leave
                "leave_start_date": leave.leave_start_date,  # Start date of the leave
                "leave_end_date": leave.leave_end_date,  # End date of the leave
                "number_of_days":int(leave.number_of_days),
                "leave_status": leave.leave_status,  # Status of the leave
                "tablename":leave.tablename,
                "emp-join-field": {"name": "leaves", "parent": leave.empid}  # Linking leave to employee
            }
        }
es = Elasticsearch([{'host': '192.168.1.27', 'port': 9200, 'scheme': 'http'}])
# es = Elasticsearch([{'host': 'localhost', 'port': 9200, 'scheme': 'http'}])


parent_child_index = 'employee-parent-child26'

try:
    employees, family_members, addresses, salarys, joiningDate, leave_records = load_csv_files()
    response = helpers.bulk(es, generate_bulk_data_for_parent_child(employees, family_members, addresses,salarys,joiningDate,leave_records,parent_child_index))
    print("Data indexed successfully!")
    print(f"Indexed {response[0]} documents successfully.")
    if response[1]:
        print(f"Failed to index {len(response[1])} documents.")
        for error in response[1]:
            print(error)
except Exception as e:
    print(f"Error indexing data: {e}")




