import React from 'react';

import Polarity from "../components/Polarity";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import Paper from "material-ui/Paper";
import TextField from "material-ui/TextField";
import RaisedButton from "material-ui/RaisedButton";
import {authHeader} from "../helpers/auth-header";

const style = {
    marginLeft: 12,
};

class PolarityPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            sentence: '',
            polarity: undefined
        };
    };

    analyzeSentence() {
        let headers = authHeader();
        headers["Content-Type"] = 'application/json';
        fetch('/webapp/sentiment', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({sentence: this.textField.getValue()})
        })
            .then(response => response.json())
            .then(data => this.setState(data));
    }

    onEnterPress = e => {
        if (e.key === 'Enter') {
            this.analyzeSentence();
        }
    };

    render() {
        const polarityComponent = this.state.polarity !== undefined ?
            <Polarity sentence={this.state.sentence} polarity={this.state.polarity}/> :
            null;

        return (
            <MuiThemeProvider>
                <div className="centerize">
                    <Paper zDepth={1} className="content">
                        <h2>Sentiment Analyser</h2>
                        <TextField ref={ref => this.textField = ref} onKeyUp={this.onEnterPress.bind(this)}
                                   hintText="Type your sentence."/>
                        <RaisedButton  label="Send" style={style} onClick={this.analyzeSentence.bind(this)}/>
                        {polarityComponent}
                    </Paper>
                </div>
            </MuiThemeProvider>
        );
    }
}

export { PolarityPage };