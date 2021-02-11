from rdflib import Variable, Literal, URIRef
from rdflib.namespace import RDFS, OWL, RDF, XSD
from query_builder import QueryBuilder, OPTIONAL, OPTIONAL_BLOCK, FILTER, FOR_GRAPH, Operators, FUNCTION_EXPR
import uuid

class RDFTransformer:
    def __init__(self, node, attributes, literals, branches):
        self.node = node
        self.attributes = attributes
        self.literals = literals
        self.branches = branches

        self.root_var_name = "root"

    def build_query(self):
        query = QueryBuilder().SELECT(
            *self.__query_variables()
        ).WHERE(
            (Variable(self.root_var_name), RDF.type, URIRef(self.node)),
            *self.__build_literal_query_blocks(self.root_var_name, self.literals),
            *self.__build_branches_query_block(self.root_var_name, self.branches)
        ).build()

        return query

    def __query_variables(self):
        return [Variable(variable) for variable in self.attributes]

    def __build_branches_query_block(self, root_var_name, branches):
        return [self.__build_edge_query_block(root_var_name, branch['edges'])
                for branch in branches if branch['edges']]

    def __build_edge_query_block(self, parent_var_name, edges):
        var_name = RDFTransformer.__unique_var_name()
        edge = edges.pop(0)

        if not edges:
            return OPTIONAL_BLOCK(
                (Variable(var_name), RDF.type, URIRef(edge['target_node'])),
                (Variable(parent_var_name), URIRef(edge['relationship']), Variable(var_name)),
                *self.__build_literal_query_blocks(var_name, edge['literals'])
            )

        return OPTIONAL_BLOCK(
                (Variable(var_name), RDF.type, URIRef(edge['target_node'])),
                (Variable(parent_var_name), URIRef(edge['relationship']), Variable(var_name)),
                *self.__build_literal_query_blocks(var_name, edge['literals']),
                self.__build_edge_query_block(var_name, edges)
            )

    def __build_literal_query_blocks(self, subject_var_name, literals):
        return [self.__query_literal_block(subject_var_name, literal['property'], literal['name'])
                for literal in literals]

    def __query_literal_block(self, subject_var_name, property_uri, object_var_name):
        return OPTIONAL(
            (Variable(subject_var_name), URIRef(property_uri), Variable(object_var_name))
        )

    @staticmethod
    def __unique_var_name():
        return str(uuid.uuid4()).replace('-', '')