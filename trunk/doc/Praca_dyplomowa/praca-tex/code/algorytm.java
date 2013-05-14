begin
    insert basic elements;
    for each axiom begin
        if property axiom begin
            insert anonymous node;
            insert connections between nodes;
        end if
        if individual axiom begin
            insert anonymous node;
            insert connections between nodes;
        end if
        if class axiom begin
            if description axiom begin
                start procedure InsertDescription;
            end if
                insert connections between nodes;
        end if
    end for
insert owl:Thing element;
connect not superclassed classes with owl:Thing;
    Procedure InsertDescription
    begin
        if someValuesFrom or allValuesFrom or hasValue or  cardinality axiom begin
            insert property usage node;
            insert edges;
            start procedure InsertDescription;
        end if
        if setTypeAxiom begin
            for each descritpion node begin
                start procedure InsertDescription;
            end foreach
        if class or individual begin
            insert connection;
        end if
    end
end
