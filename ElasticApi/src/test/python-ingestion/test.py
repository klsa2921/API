import json

# Function to convert the input JSON data into Elasticsearch bulk format, including routing
def convert_to_bulk_format_with_routing(input_file, output_file):
    with open(input_file, 'r') as infile:
        data = infile.readlines()  # Read lines from the input file

    bulk_data = []

    for line in data:
        try:
            document = json.loads(line.strip())  # Parse each line into a JSON object
            op_type = document.get('_op_type', 'index')  # Get operation type (default to 'index')
            routing = document.get('_routing', None)  # Get routing value (if present)
            
            # Prepare the index action with routing if it exists
            index_data = {
                op_type: {
                    "_index": document['_index'],
                    "_id": document['_id']
                }
            }

            # If _routing is provided, include it in the action
            if routing:
                index_data[op_type]["_routing"] = routing

            bulk_data.append(index_data)  # Add the operation part
            bulk_data.append(document['_source'])  # Add the source part
        except json.JSONDecodeError as e:
            print(f"Error parsing line: {line.strip()} -> {e}")
    
    # Write the bulk formatted data to the output file
    with open(output_file, 'w') as outfile:
        for item in bulk_data:
            json.dump(item, outfile)
            outfile.write("\n")  # Write each operation/document on a new line

# Specify the input and output file paths
input_file = 'ingest/ingest.json'  # Replace with your input file path
output_file = 'output_bulk_with_routing.json'  # Output file to store the bulk query

# Call the function to convert the file data
convert_to_bulk_format_with_routing(input_file, output_file)

print(f"Bulk data with routing has been written to {output_file}")

