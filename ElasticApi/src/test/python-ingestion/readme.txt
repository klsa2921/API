1.Before Testing the search queries run the main.py python file.change the parent_child_index
to the index you want to ingest the data.
                    or
 You can use the  kibana dev console queries present in this file ingest/ingest.json.run them in the kibana dev console.

Note:-Use the below authentication to access the endpoints:

username    password
========    ========
admin       klsa
superuser   super

To test the below endpoints use the queries provided:
1.for /api/searchByUsingClient query is
    {
        "bool": {
            "must": [
                {
                    "has_child": {
                        "type": "leaves",
                        "query": {
                            "range": {
                                "leave_start_date": {
                                    "gte": "2025-01-01",
                                    "lte": "2025-01-31"
                                }
                            }
                        },
                        "inner_hits": {
                            "name": "january_leaves",
                            "size": 10
                        }
                    }
                },
                {
                    "has_child": {
                        "type": "leaves",
                        "query": {
                            "range": {
                                "leave_start_date": {
                                    "gte": "2025-08-01",
                                    "lte": "2025-08-31"
                                }
                            }
                        },
                        "inner_hits": {
                            "name": "august_leaves",
                            "size": 10
                        }
                    }
                }
            ]
        }
    }
2. For /api/searchByUsingRestAPI use
    {
        "query":{
            "bool": {
                "must": [
                    {
                        "has_child": {
                            "type": "leaves",
                            "query": {
                                "range": {
                                    "leave_start_date": {
                                        "gte": "2025-01-01",
                                        "lte": "2025-01-31"
                                    }
                                }
                            },
                            "inner_hits": {
                                "name": "january_leaves",
                                "size": 10
                            }
                        }
                    },
                    {
                        "has_child": {
                            "type": "leaves",
                            "query": {
                                "range": {
                                    "leave_start_date": {
                                        "gte": "2025-08-01",
                                        "lte": "2025-08-31"
                                    }
                                }
                            },
                            "inner_hits": {
                                "name": "august_leaves",
                                "size": 10
                            }
                        }
                    }
                ]
            }
        }
    }

3. For /api/searchByUsingClientWithFilter use
    {
        "bool": {
            "must": [
              {
                "range": {
                  "age": {
                    "gte": 30,
                    "lte": 60
                  }
                }
              }
            ]
        }
    }

4.For /api/searchWithFilterByUsingRestAPI
    {
        "query":{
            "bool": {
                "must": [
                  {
                    "range": {
                      "age": {
                        "gte": 30,
                        "lte": 60
                      }
                    }
                  }
                ]
            }
        }
    }