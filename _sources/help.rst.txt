Help
----
Each command contains a --help option::

    >>> geoc vector buffer --help
    geoc vector buffer: Buffer the features of the input Layer and save them to the output Layer
    --help                      : Print the help message
    -c (--capstyle) VAL         : The cap style
    -d (--distance) VAL         : The buffer distance
    -i (--input-workspace) VAL  : The input workspace
    -l (--input-layer) VAL      : The input layer
    -o (--output-workspace) VAL : The output workspace
    -q (--quadrantsegments) N   : The number of quadrant segments
    -r (--output-layer) VAL     : The output layer
    -s (--singlesided)          : Whether buffer should be single sided or not

There is also a man page for each subcommand::

    >>> man geoc-vector-buffer
    geoc-vector-buffer(1)                                    geoc-vector-buffer(1)

    NAME
           geoc vector buffer

    DESCRIPTION
           Buffer  the  features  of  the  input Layer and save them to the output
           Layer

    USAGE
           geoc vector randompoints -n 10 -g "1,1,10,10" | geoc vector  buffer  -d
           10

    OPTIONS
           -d --distance: The buffer distance

           -q --quadrantsegments: The number of quadrant segments

           -s --singlesided: Whether buffer should be single sided or not

           -c --capstyle: The cap style

           -o --output-workspace: The output workspace

Finally, there is a bash completion script which makes using geoc with bash much easier.

Install it is your .bash_profile::
    
    source /Users/You/geoc/shell/geoc_bash_comp
